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

import java.io.IOException;

import io.lindb.client.internal.WriteClient;

/**
 * Write api create factory.
 */
public class WriteFactory {

	/**
	 * Retrun an async write api based on given write options and http client.
	 * 
	 * @param options  write options
	 * @param client   http write client
	 * @param listener the listener to listen events
	 *
	 * @return write api {@link Write}
	 * @throws IOException create error
	 */
	public static Write createWrite(WriteOptions options, WriteClient client, EventListener listener)
			throws IOException {
		return new WriteImpl(options, client, listener);
	}

	/**
	 * Retrun a blocking write api based on given write options and http client.
	 * 
	 * @param options write options
	 * @param client  http write client
	 *
	 * @return write api {@link BlockingWrite}
	 * @throws IOException create error
	 */
	public static BlockingWrite createBlockingWrite(WriteOptions options, WriteClient client)
			throws IOException {
		return new BlockingWriteImpl(options, client);
	}

	private WriteFactory() {
	}
}
