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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lindb.client.internal.HttpClient;
import io.lindb.client.internal.HttpOptions;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;

public class WriteImplTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(WriteImplTest.class);
	private HttpClient client;

	@Before
	public void setup() {
		client = new HttpClient("http://localhost:9000", HttpOptions.builder().build());
	}

	@Test
	public void put() throws Exception {
		WriteOptions options = WriteOptions.builder().flushInterval(1).batchQueue(1).build();
		WriteImpl write = new WriteImpl(options, client, false,
				(event, e) -> LOGGER.error("on error, event {}", event, e));
		Thread.sleep(10); // wait consume thread pause
		Point point = Point.builder("test").addSum("sum", 1.0).build();
		assertTrue(write.put(point));
		assertFalse(write.put(point));
		long now = System.currentTimeMillis();
		assertFalse(write.put(point, 100, TimeUnit.MILLISECONDS));
		long diff = System.currentTimeMillis() - now;
		assertTrue(diff >= 100);

		// test put after stop
		options = WriteOptions.builder().flushInterval(1).batchQueue(1000).build();
		write = new WriteImpl(options, client);
		write.close(); // close write

		Thread.sleep(10); // wait consume thread pause
		point = Point.builder("test").addSum("sum", 1.0).build();
		assertFalse(write.put(point));
		now = System.currentTimeMillis();
		assertFalse(write.put(point, 100, TimeUnit.MILLISECONDS));
		diff = System.currentTimeMillis() - now;
		assertTrue(diff < 100);
	}

	@Test
	public void putInvalidPoint() throws Exception {
		WriteOptions options = WriteOptions.builder().flushInterval(1).batchQueue(1).build();
		WriteImpl write = new WriteImpl(options, client);
		try {
			assertTrue(write.put(null));
			assertTrue(write.put(null, 100, TimeUnit.MILLISECONDS));
			Point point = Point.builder("test").build();
			assertTrue(write.put(point));
			assertTrue(write.put(point, 100, TimeUnit.MILLISECONDS));
		} finally {
			write.close();
		}
	}

	@Test
	public void decodeFailure() throws Exception {
		WriteOptions options = WriteOptions.builder().batchSize(1).build();
		Point point = Point.builder("test").addField(new MockField()).build();
		WriteImpl write = new WriteImpl(options, client, (event, e) -> LOGGER.error("on error, event {}", event, e));
		try {
			write.put(point);
			Thread.sleep(30);
		} finally {
			write.close();
		}
	}

	@Test
	public void decodeConsume() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				Thread.sleep(10);
				return new MockResponse().setResponseCode(200);
			}
		};
		server.setDispatcher(dispatcher);
		HttpClient client = new HttpClient(server.url("/test").toString(), HttpOptions.builder().build());

		WriteOptions options = WriteOptions.builder().flushInterval(10).maxRetries(2).batchSize(20).build();
		Point point = Point.builder("test").addLast("last", 1.0).build();
		WriteImpl write = new WriteImpl(options, client);
		try {
			// test > flush interval
			write.put(point);
			Thread.sleep(50);
			for (int i = 0; i < 8; i++) {
				write.put(point);
			}
		} finally {
			write.close();
			server.close();
		}
	}

	@Test
	public void sendLastDataWhenClose() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {
			int count = 0;

			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				if (count == 0) {
					Thread.sleep(100);
				}
				count++;
				return new MockResponse().setResponseCode(200);
			}
		};
		server.setDispatcher(dispatcher);
		HttpClient client = new HttpClient(server.url("/test").toString(), HttpOptions.builder().build());

		WriteOptions options = WriteOptions.builder().flushInterval(10).sendQueue(10).maxRetries(2).batchSize(1)
				.build();
		Point point = Point.builder("test").addLast("last", 1.0).build();
		WriteImpl write = new WriteImpl(options, client);
		try {
			for (int i = 0; i < 8; i++) {
				write.put(point);
			}
			Thread.sleep(50);
		} finally {
			write.close();
			server.close();
		}
	}

	@Test
	public void retryFailure() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {
			int count = 0;

			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				try {
					if (count % 2 == 0) {
						return new MockResponse().setResponseCode(500);
					} else {
						return new MockResponse().setResponseCode(500);
					}
				} finally {
					count++;
				}
			}
		};
		server.setDispatcher(dispatcher);

		HttpClient client = new HttpClient(server.url("/test").toString(), HttpOptions.builder().build());

		WriteOptions options = WriteOptions.builder().useGZip(false).flushInterval(30).maxRetries(2).batchSize(1)
				.build();
		Point point = Point.builder("test").addLast("last", 1.0).build();
		WriteImpl write = new WriteImpl(options, client, (event, e) -> LOGGER.error("on error, event {}", event, e));
		try {
			// test > batch size
			write.put(point);
			write.put(point);
			write.put(point);
			write.put(point);
			Thread.sleep(100);
		} finally {
			write.close();
			server.close();
		}
	}

	@Test
	public void retrySuccess() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {
			int count = 0;

			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				try {
					if (count == 0) {
						// first failure, retry
						return new MockResponse().setResponseCode(500);
					} else {
						return new MockResponse().setResponseCode(200);
					}
				} finally {
					count++;
				}
			}
		};
		server.setDispatcher(dispatcher);

		HttpClient client = new HttpClient(server.url("/test").toString(), HttpOptions.builder().build());

		WriteOptions options = WriteOptions.builder().useGZip(false).flushInterval(30).maxRetries(2).batchSize(1)
				.build();
		Point point = Point.builder("test").addLast("last", 1.0).build();
		WriteImpl write = new WriteImpl(options, client);
		try {
			// test > batch size
			write.put(point);
			write.put(point);
			Thread.sleep(100);
		} finally {
			write.close();
			server.close();
		}
	}

	@Test
	public void retryQueueFull() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				Thread.sleep(10);
				return new MockResponse().setResponseCode(500);
			}
		};
		server.setDispatcher(dispatcher);
		HttpClient client = new HttpClient(server.url("/test").toString(), HttpOptions.builder().build());

		WriteOptions options = WriteOptions.builder().flushInterval(30).retryQueue(1).maxRetries(2).batchSize(1)
				.build();
		Point point = Point.builder("test").addLast("last", 1.0).build();
		WriteImpl write = new WriteImpl(options, client, (event, e) -> LOGGER.error("on error, event {}", event, e));
		try {
			// test > batch size
			write.put(point);
			write.put(point);
			write.put(point);
			write.put(point);
			write.put(point);
			Thread.sleep(100);
		} finally {
			write.close();
			server.close();
		}
	}

	@Test
	public void throwExceptionWhenSendData() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {
			int count = 0;

			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				try {
					if (count == 0) {
						return new MockResponse().setResponseCode(500);
					} else {
						// mock exception
						return new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START);
					}
				} finally {
					count++;
				}
			}
		};
		server.setDispatcher(dispatcher);
		HttpClient client = new HttpClient(server.url("/test").toString(), HttpOptions.builder().build());

		WriteOptions options = WriteOptions.builder().flushInterval(30).retryQueue(1).maxRetries(2).batchSize(1)
				.build();
		Point point = Point.builder("test").addLast("last", 1.0).build();
		WriteImpl write = new WriteImpl(options, client);
		try {
			// test > batch size
			write.put(point);
			write.put(point);
			write.put(point);
			write.put(point);
			write.put(point);
			Thread.sleep(100);
		} finally {
			write.close();
			server.close();
		}
	}

	@Test
	public void closeSendFailure() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				// mock exception
				return new MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START);
			}
		};
		server.setDispatcher(dispatcher);
		HttpClient client = new HttpClient(server.url("/test").toString(), HttpOptions.builder().build());

		WriteOptions options = WriteOptions.builder().retryQueue(1).maxRetries(2)
				.build();
		Point point = Point.builder("test").addLast("last", 1.0).build();
		WriteImpl write = new WriteImpl(options, client);
		try {
			write.put(point);
			write.put(point);
			write.put(point);
			Thread.sleep(100);
		} finally {
			write.close();
			server.close();
		}
	}

	@Test
	public void closeSendOk() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				return new MockResponse().setResponseCode(200);
			}
		};
		server.setDispatcher(dispatcher);
		HttpClient client = new HttpClient(server.url("/test").toString(), HttpOptions.builder().build());

		WriteOptions options = WriteOptions.builder().retryQueue(1).maxRetries(2)
				.build();
		Point point = Point.builder("done").addLast("last", 1.0).build();
		WriteImpl write = new WriteImpl(options, client);
		try {
			write.put(point);
			write.put(point);
			Thread.sleep(100);
		} finally {
			write.close();
			server.close();
		}
	}
}
