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

/**
 * LinDB client factory for creating LinDB client.
 */
public final class ClientFactory {
	private ClientFactory() {
	}

	/**
	 * Create LinDB client by given broker endpoint with default options.
	 * 
	 * @param brokerEndpoint broker endpoint{@link String}
	 * @return {@link Client}
	 */
	public static Client create(String brokerEndpoint) {
		// new default options
		Options options = Options.builder().build();
		return new ClientImpl(brokerEndpoint, options);
	}

	/**
	 * Create LinDB client by given broker endpoing and options.
	 *
	 * @param brokerEndpoint broker endpoint {@link String}
	 * @param options        client options {@link Options}
	 * @return {@link Client}
	 */
	public static Client create(String brokerEndpoint, Options options) {
		return new ClientImpl(brokerEndpoint, options);
	}
}
