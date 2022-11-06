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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Preconditions;

import io.lindb.client.util.StringUtils;

/**
 * Write api configuration options.
 */
public final class WriteOptions {
	@NotThreadSafe
	public static class Builder {
		private int batchSize = DEFAULT_BATCH_SIZE;
		private long flushInterval = DEFAULT_FLUSH_INTERVAL;
		private boolean useGZip = DEFAULT_USE_GZIP;
		private Map<String, String> defaultTags;
		private int maxRetries = DEFAULT_MAX_RETRIES;
		private int retryQueue = DEFAULT_RETRY_QUEUE;
		private int batchQueue = DEFAULT_BATCH_QUEUE;
		private int sendQueue = DEFAULT_SEND_QUEUE;

		private Builder() {
		}

		/**
		 * Return the write options with setting.
		 * 
		 * @return write options
		 */
		public WriteOptions build() {
			return new WriteOptions(this);
		}

		/**
		 * Set write batch size.
		 * Default value: 1000.
		 * 
		 * @param batchSize {@link long}
		 * @return builder
		 */
		public Builder batchSize(final int batchSize) {
			Preconditions.checkArgument(batchSize > 0, "negative batch size", batchSize);
			this.batchSize = batchSize;
			return this;
		}

		/**
		 * Set buffer flush interval.
		 * Default value: 1000ms.
		 * 
		 * @param flushInterval {@link long}
		 * @return builder
		 */
		public Builder flushInterval(final long flushInterval) {
			Preconditions.checkArgument(flushInterval > 0, "negative flush interval", flushInterval);
			this.flushInterval = flushInterval;
			return this;
		}

		/**
		 * Set buffer data if using gzip compression.
		 * Default value: true.
		 * 
		 * @param useGZip {@link boolean}
		 * @return builder
		 */
		public Builder useGZip(final boolean useGZip) {
			this.useGZip = useGZip;
			return this;
		}

		/**
		 * Set data point default tags.
		 * 
		 * @param key   {@link String}
		 * @param value {@link String}
		 * @return builder
		 */
		public Builder addDefaultTag(final String key, final String value) {
			Preconditions.checkArgument(StringUtils.isNotEmpty(key), "tag key cannot be empty");
			Preconditions.checkArgument(StringUtils.isNotEmpty(value), "tag value cannot be empty");

			if (null == this.defaultTags) {
				this.defaultTags = new HashMap<>();
			}

			this.defaultTags.put(key, value);
			return this;
		}

		/**
		 * Set max retry count if send data point failure.
		 * Default value: 3.
		 * 
		 * @param maxRetries {@link long}
		 * @return builder
		 */
		public Builder maxRetries(final int maxRetries) {
			Preconditions.checkArgument(maxRetries > 0, "negative max retries", maxRetries);
			this.maxRetries = maxRetries;
			return this;
		}

		/**
		 * Set retry queue size.
		 * Default value: 100.
		 * 
		 * @param retryQueue {@link long}
		 * @return builder
		 */
		public Builder retryQueue(final int retryQueue) {
			Preconditions.checkArgument(retryQueue > 0, "negative retry queue size", retryQueue);
			this.retryQueue = retryQueue;
			return this;
		}

		/**
		 * Set batch queue size.
		 * Default value: 1024.
		 * 
		 * @param batchQueue {@link long}
		 * @return builder
		 */
		public Builder batchQueue(final int batchQueue) {
			Preconditions.checkArgument(batchQueue > 0, "negative batch queue size", batchQueue);
			this.batchQueue = batchQueue;
			return this;
		}

		/**
		 * Set send queue size.
		 * Default value: 1024.
		 * 
		 * @param sendQueue {@link long}
		 * @return builder
		 */
		public Builder sendQueue(final int sendQueue) {
			Preconditions.checkArgument(sendQueue > 0, "negative send queue size", sendQueue);
			this.sendQueue = sendQueue;
			return this;
		}
	}

	public static final int DEFAULT_BATCH_SIZE = 1_000;
	public static final int DEFAULT_BATCH_QUEUE = 1024;
	public static final int DEFAULT_SEND_QUEUE = 1024;
	public static final long DEFAULT_FLUSH_INTERVAL = 1_000;
	public static final boolean DEFAULT_USE_GZIP = true;
	public static final int DEFAULT_MAX_RETRIES = 3;
	public static final int DEFAULT_RETRY_QUEUE = 1_00;

	/**
	 * Create write options builder instance
	 * 
	 * @return builder
	 */
	public static Builder builder() {
		return new Builder();
	}

	private int batchSize;
	private int batchQueue;
	private int sendQueue;
	private long flushInterval;
	private boolean useGZip;
	private Map<String, String> defaultTags;
	private int maxRetries;
	private int retryQueue;

	private WriteOptions(final Builder builder) {
		this.batchSize = builder.batchSize;
		this.flushInterval = builder.flushInterval;
		this.useGZip = builder.useGZip;
		this.defaultTags = builder.defaultTags;
		this.maxRetries = builder.maxRetries;
		this.retryQueue = builder.retryQueue;
		this.batchQueue = builder.batchQueue;
		this.sendQueue = builder.sendQueue;
	}

	/**
	 * Return the send batch size.
	 * 
	 * @return batch size
	 */
	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * Retry the max retry count if send failure.
	 * 
	 * @return max retry
	 */
	public int getMaxRetries() {
		return maxRetries;
	}

	/**
	 * Return flush buffer data interval.
	 * 
	 * @return interval
	 */
	public long getFlushInterval() {
		return flushInterval;
	}

	/**
	 * Return the default tags.
	 * 
	 * @return tags
	 */
	public Map<String, String> getDefaultTags() {
		return defaultTags;
	}

	/**
	 * Return if useing gzip comress data.
	 * 
	 * @return if true use gzi
	 */
	public boolean isUseGZip() {
		return useGZip;
	}

	/**
	 * Return the size of batch queue.
	 * 
	 * @return queue size
	 */
	public int getBatchQueue() {
		return batchQueue;
	}

	/**
	 * Return the size of send queue.
	 * 
	 * @return queue size
	 */
	public int getSendQueue() {
		return sendQueue;
	}

	/**
	 * Return the size of retry queue.
	 * 
	 * @return queue size
	 */
	public int getRetryQueue() {
		return retryQueue;
	}

}
