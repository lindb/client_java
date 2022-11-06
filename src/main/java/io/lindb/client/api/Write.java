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

import java.util.concurrent.TimeUnit;

/**
 * Write metric poing api.
 */
public interface Write {

	/**
	 * Put metric point.
	 * 
	 * @param point metric data point
	 * @return if put successfully {@link boolean}
	 */
	boolean put(Point point);

	/**
	 * Put metric point with timeout.
	 * 
	 * @param point   metric data point
	 * @param timeout put timeput
	 * @param unit    unit of timeout
	 * @return if put successfully {@link boolean}
	 * @throws InterruptedException thread interrupted
	 */
	boolean put(Point point, long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * Close write, release resources.
	 * 
	 * @throws Exception close error
	 */
	void close() throws Exception;
}
