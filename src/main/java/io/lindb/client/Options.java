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
package io.lindb.client;

import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Preconditions;

import io.lindb.client.api.WriteOptions;
import io.lindb.client.internal.HttpOptions;
import io.lindb.client.util.StringUtils;

/**
 * Options are used to configure write/http options.
 */
public final class Options {

	/**
	 * LinDB client options builder.
	 */
	@NotThreadSafe
	public static class Builder {
		private HttpOptions.Builder httpOptions;
		private WriteOptions.Builder writeOptions;

		private Builder() {
			this.httpOptions = HttpOptions.builder();
			this.writeOptions = WriteOptions.builder();
		}

		/**
		 * Set http client connect timeout.
		 * Default value: 30s.
		 * 
		 * @param timeout {@link long}
		 * @return builder
		 */
		public Builder connectTimeout(long timeout) {
			Preconditions.checkArgument(timeout > 0, "negative connect timeout", timeout);
			this.httpOptions.connectTimeout(timeout);
			return this;
		}

		/**
		 * Set http client write timeout.
		 * Default value: 30s.
		 * 
		 * @param timeout {@link long}
		 * @return builder
		 */
		public Builder writeTimeout(long timeout) {
			Preconditions.checkArgument(timeout > 0, "negative write timeout", timeout);
			this.httpOptions.writeTimeout(timeout);
			return this;
		}

		/**
		 * Set http client read timeout.
		 * Default value: 30s.
		 * 
		 * @param timeout {@link long}
		 * @return builder
		 */
		public Builder readTimeout(long timeout) {
			Preconditions.checkArgument(timeout > 0, "negative read timeout", timeout);
			this.httpOptions.readTimeout(timeout);
			return this;
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
			this.writeOptions.batchSize(batchSize);
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
			this.writeOptions.flushInterval(flushInterval);
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
			this.writeOptions.useGZip(useGZip);
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
			this.writeOptions.addDefaultTag(key, value);
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
			this.writeOptions.maxRetries(maxRetries);
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
			this.writeOptions.retryQueue(retryQueue);
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
			this.writeOptions.batchQueue(batchQueue);
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
			this.writeOptions.sendQueue(sendQueue);
			return this;
		}

		/**
		 * Return http/write options
		 * 
		 * @return options {@link Options}
		 */
		public Options build() {
			Options options = new Options();
			options.httpOptions = this.httpOptions.build();
			options.writeOptions = this.writeOptions.build();
			return options;
		}
	}

	/**
	 * Return options builder
	 * 
	 * @return bulider {@link Builder}
	 */
	public static Builder builder() {
		return new Builder();
	}

	private HttpOptions httpOptions;
	private WriteOptions writeOptions;

	private Options() {
	}

	/**
	 * Return http client options
	 * 
	 * @return http options {@link HttpOptions}
	 */
	public HttpOptions getHttpOptions() {
		return httpOptions;
	}

	/**
	 * Return metric write options
	 * 
	 * @return write options {@link WriteOptions}
	 */
	public WriteOptions getWriteOptions() {
		return writeOptions;
	}
}
