/**
 * Licensed to LinDB under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. LinDB licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.lindb.client.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPOutputStream;

import javax.annotation.concurrent.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lindb.client.internal.WriteClient;

/**
 * Implement an async write api.
 */
@ThreadSafe
public class WriteImpl implements Write {
	private final static Logger LOGGER = LoggerFactory.getLogger(WriteImpl.class);
	private final WriteOptions options;
	private final boolean useGZip;
	private final long flushInterval;
	private final int batchSize;
	private final int maxRetry;
	private final Map<String, String> defaultTags;

	private BlockingQueue<Point> points;
	private BlockingQueue<byte[]> sendBuffers;
	private BlockingQueue<RetryEntry> retryQueue;
	private RowBuilder builder;
	private ByteArrayOutputStream buffer;
	private WriteClient client;
	private AtomicBoolean running;
	private EventListener listener;
	private final CountDownLatch latch;

	/**
	 * Create a write api instance with options and http client.
	 * 
	 * @param options write options
	 * @param client  http client
	 * @throws IOException create error
	 */
	protected WriteImpl(WriteOptions options, WriteClient client) throws IOException {
		this(options, client, null);
	}

	/**
	 * Create a write api instance with options and http client.
	 * 
	 * @param options  write options
	 * @param client   http write client
	 * @param listener the listener to listen events
	 * @throws IOException create error
	 */
	protected WriteImpl(WriteOptions options, WriteClient client, EventListener listener) throws IOException {
		this(options, client, true, listener);
	}

	/**
	 * Create a write api instance with options and http client.
	 * 
	 * @param options write options
	 * @param client  http write client
	 * @param startup if startup consumer threads
	 * @throws IOException create error
	 */
	protected WriteImpl(WriteOptions options, WriteClient client, boolean startup) throws IOException {
		this(options, client, startup, null);
	}

	/**
	 * Create a write api instance with options and http client.
	 * 
	 * @param options  write options
	 * @param client   http write client
	 * @param startup  if startup consumer threads
	 * @param listener the listener to listen events
	 * @throws IOException create error
	 */
	protected WriteImpl(WriteOptions options, WriteClient client, boolean startup, EventListener listener)
			throws IOException {
		this.options = options;
		this.useGZip = options.isUseGZip();
		this.flushInterval = options.getFlushInterval();
		this.batchSize = options.getBatchSize();
		this.maxRetry = options.getMaxRetries();
		this.defaultTags = options.getDefaultTags();

		this.client = client;
		this.points = new ArrayBlockingQueue<>(this.options.getBatchQueue());
		this.sendBuffers = new ArrayBlockingQueue<>(this.options.getSendQueue());
		this.retryQueue = new ArrayBlockingQueue<>(this.options.getRetryQueue());
		this.buffer = new ByteArrayOutputStream();
		this.builder = new RowBuilder();
		this.running = new AtomicBoolean(true);
		this.listener = listener;

		if (startup) {
			this.startup();
		}

		latch = new CountDownLatch(3);
	}

	private void startup() throws IOException {
		// decode process thread
		Thread decodeProc = new Thread(new DecodeConsumer());
		decodeProc.setName("lin-decoder");
		decodeProc.setDaemon(true);
		decodeProc.start();

		// send process thread
		Thread sendProc = new Thread(new SendConsumer());
		sendProc.setName("lin-sender");
		sendProc.setDaemon(true);
		sendProc.start();

		// retry send process thread
		Thread resendProc = new Thread(new RetryConsumer());
		resendProc.setName("lin-re-sender");
		resendProc.setDaemon(true);
		resendProc.start();
	}

	/**
	 * Put metric point.
	 * 
	 * @param point metric data point
	 * @return if put successfully {@link boolean}
	 */
	@Override
	public boolean put(Point point) {
		if (!this.running.get()) {
			LOGGER.warn("write closed.");
			return false;
		}
		if (point == null || !point.validate()) {
			return true;
		}
		return this.points.offer(point);
	}

	/**
	 * Put metric point with timeout.
	 * 
	 * @param point   metric data point
	 * @param timeout put timeput
	 * @param unit    unit of timeout
	 * @return if put successfully {@link boolean}
	 * @throws InterruptedException thread interrupted
	 */
	@Override
	public boolean put(Point point, long timeout, TimeUnit unit) throws InterruptedException {
		if (!this.running.get()) {
			LOGGER.warn("write closed.");
			return false;
		}
		if (point == null || !point.validate()) {
			return true;
		}
		return this.points.offer(point, timeout, unit);
	}

	class DecodeConsumer implements Runnable {
		private int batch = 0;

		private void batch(Point point) throws IOException {
			try {
				byte[] data = builder.build(point, defaultTags);
				buffer.write(data);
				batch++;
			} finally {
				builder.reset();
			}
		}

		@Override
		public void run() {
			long nextFlush = System.currentTimeMillis() + flushInterval;
			while (running.get()) {
				try {
					long now = System.currentTimeMillis();
					long diff = nextFlush - now;
					Point point = null;
					if (diff > 0) {
						// not reach next flush interval, poll with timeout
						point = points.poll(diff, TimeUnit.MILLISECONDS);
					} else {
						point = points.poll();
					}
					if (point != null) {
						// if point not null, batch it.
						batch(point);
					}

					// check if need to send
					if (batch >= batchSize || (batch > 0 && diff <= 0)) {
						byte[] d = buffer.toByteArray();
						buffer.reset();
						sendBuffers.put(d);
						batch = 0;
						nextFlush = now + flushInterval;
					} else if (diff <= 0) {
						nextFlush = now + flushInterval;
					}
				} catch (Throwable e) {
					LOGGER.error("decode data point failure", e);
					onError(EventType.decode, e);
				} finally {
					builder.reset();
				}
			}

			try {
				if (!points.isEmpty()) {
					Point[] pendingPoints = points.toArray(new Point[0]);
					for (Point point : pendingPoints) {
						batch(point);
					}
				}
				if (batch > 0) {
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					sendData(outputStream, buffer.toByteArray());
				}
			} catch (Exception e) {
				LOGGER.error("send last data failure when write close", e);
				onError(EventType.send, e);
			}

			latch.countDown();
		}
	}

	class SendConsumer implements Runnable {
		private ByteArrayOutputStream outputStream;

		public SendConsumer() throws IOException {
			if (useGZip) {
				outputStream = new ByteArrayOutputStream();
			}
		}

		@Override
		public void run() {
			while (running.get() || (!running.get() && !sendBuffers.isEmpty())) {
				try {
					byte[] data = sendBuffers.take();
					if (data.length == 0) {
						// write closing
						continue;
					}
					if (!sendData(outputStream, data)) {
						if (!retryQueue.offer(new RetryEntry(data))) {
							LOGGER.warn("cannot put data into retry queue ignore this data when send failure");
							onError(EventType.retry, new RuntimeException("cannot put retry queue"));
						}
					}
				} catch (Throwable e) {
					LOGGER.error("send data point failure", e);
					onError(EventType.send, e);
				}
			}
			latch.countDown();
		}
	}

	class RetryConsumer implements Runnable {

		@Override
		public void run() {
			while (running.get() || (!running.get() && !retryQueue.isEmpty())) {
				try {
					RetryEntry entry = retryQueue.take();
					if (entry.getData() == null) {
						// write closing
						continue;
					}
					entry.increaseRetry();
					if (!client.writeMetric(entry.getData(), useGZip)) {
						if (entry.getRetry() < maxRetry) {
							if (!retryQueue.offer(entry)) {
								LOGGER.warn("cannot put data into retry queue ignore this data when re-send failure");
								onError(EventType.retry, new RuntimeException("retry too many times"));
							}
						} else {
							LOGGER.warn("retry too many times ignore this data");
						}
					}
				} catch (Throwable e) {
					LOGGER.error("re-send data point failure", e);
					onError(EventType.send, e);
				}
			}
			latch.countDown();
		}
	}

	/*
	 * Close write, stop all consumer process.
	 * 
	 * @see io.lindb.client.api.Write#close()
	 */
	@Override
	public void close() throws Exception {
		this.running.set(false);
		// trigger consume thread close.
		this.sendBuffers.put(new byte[] {});
		this.retryQueue.put(new RetryEntry(null));

		latch.await(15, TimeUnit.SECONDS);
	}

	private boolean sendData(ByteArrayOutputStream outputStream, byte[] data) throws Exception {
		if (useGZip) {
			GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
			// compress using gzip
			gzip.write(data);
			gzip.close();
			data = outputStream.toByteArray();
			outputStream.reset();
		}

		return client.writeMetric(data, useGZip);
	}

	/**
	 * Invoke when throw exception
	 * 
	 * @param event event type
	 * @param e     expcetion
	 */
	protected void onError(EventType event, final Throwable e) {
		if (this.listener != null) {
			this.listener.onError(event, e);
		}
	}

}
