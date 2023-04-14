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

import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Data write entry include data and retry count.
 */
@NotThreadSafe
public class WriteEntry {
	private final byte[] data;
	private final List<Point> points;
	private int retry;

	/**
	 * Create write entry instance with data.
	 * 
	 * @param data   data of points
	 * @param points batched points
	 */
	protected WriteEntry(byte[] data, List<Point> points) {
		this.data = data;
		this.points = points;
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
	 * @return data of points
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Return write points
	 * 
	 * @return write points
	 */
	public List<Point> getPoints() {
		return points;
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
