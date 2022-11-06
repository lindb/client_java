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

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Data retry entry include data and retry count.
 */
@NotThreadSafe
public class RetryEntry {
	private final byte[] data;
	private int retry;

	/**
	 * Create retryn entry instance with data.
	 * 
	 * @param data data of point
	 */
	protected RetryEntry(byte[] data) {
		this.data = data;
	}

	/**
	 * Increase retry count.
	 */
	public void increaseRetry() {
		this.retry++;
	}

	/**
	 * Return the retry data.
	 * 
	 * @return data of point
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Return the retry count.
	 * 
	 * @return retry count
	 */
	public int getRetry() {
		return retry;
	}
}
