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
package io.lindb.client.internal;

import javax.annotation.concurrent.NotThreadSafe;

import com.google.common.base.Preconditions;

/**
 * Http client configuration options.
 */
public final class HttpOptions {

	/**
	 * Http client options builder.
	 */
	@NotThreadSafe
	public static class Builder {
		private HttpOptions options;

		/**
		 * Http client options builder
		 */
		private Builder() {
			this.options = new HttpOptions();
			this.options.connectTimeout = DEFAULT_TIMEOUT;
			this.options.writeTimeout = DEFAULT_TIMEOUT;
			this.options.readTimeout = DEFAULT_TIMEOUT;
		}

		/**
		 * Set http client connect timeout.
		 * 
		 * @param timeout {@link long}
		 * @return builder
		 */
		public Builder connectTimeout(long timeout) {
			Preconditions.checkArgument(timeout > 0, "negative connect timeout: %d", timeout);
			this.options.connectTimeout = timeout;
			return this;
		}

		/**
		 * Set http client write timeout
		 * 
		 * @param timeout {@link long}
		 * @return builder
		 */
		public Builder writeTimeout(long timeout) {
			Preconditions.checkArgument(timeout > 0, "negative write timeout: %d", timeout);
			this.options.writeTimeout = timeout;
			return this;
		}

		/**
		 * Set http client read timeout
		 * 
		 * @param timeout {@link long}
		 * @return builder
		 */
		public Builder readTimeout(long timeout) {
			Preconditions.checkArgument(timeout > 0, "negative read timeout: %d", timeout);
			this.options.readTimeout = timeout;
			return this;
		}

		/**
		 * Rreturn http options based on settings.
		 * 
		 * @return http options
		 */
		public HttpOptions build() {
			return this.options;
		}
	}

	/**
	 * Default timeout for connect/read/write
	 */
	public static final long DEFAULT_TIMEOUT = 30;

	/**
	 * Create a http client options builder
	 * 
	 * @return builder {@link Builder}
	 */
	public static Builder builder() {
		return new Builder();
	}

	private long connectTimeout;
	private long readTimeout;
	private long writeTimeout;

	private HttpOptions() {
	}

	/**
	 * Return connect timeout
	 * 
	 * @return connect timeout
	 */
	public long getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * Return read timeout
	 * 
	 * @return read timeout
	 */
	public long getReadTimeout() {
		return readTimeout;
	}

	/**
	 * Return write timeout
	 * 
	 * @return write time
	 */
	public long getWriteTimeout() {
		return writeTimeout;
	}

}
