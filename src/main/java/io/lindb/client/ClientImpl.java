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

import java.io.IOException;

import io.lindb.client.api.Write;
import io.lindb.client.api.WriteFactory;
import io.lindb.client.internal.HttpClient;

/**
 * ClientImpl implements {@link Client} interface
 */
public class ClientImpl implements Client {
	private final String brokerEndpoint;
	private final Options options;

	/**
	 * Create LinDB client instance
	 * 
	 * @param brokerEndpoint broker endpoint {@link String}
	 * @param options        write/http options {@link Options}
	 */
	protected ClientImpl(String brokerEndpoint, Options options) {
		this.brokerEndpoint = brokerEndpoint;
		this.options = options;
	}

	/*
	 * Create async write client by given database name
	 * 
	 * @see io.lindb.client.Client#write(java.lang.String)
	 * 
	 * @param database database name {@link String}
	 * 
	 * @return async write api
	 * 
	 * @throws {@link IOException}
	 *
	 */
	@Override
	public Write write(String database) throws IOException {
		String url = String.format("%s%s?db=%s", this.brokerEndpoint, Constants.WRITE_API, database);
		HttpClient client = new HttpClient(url, this.options.getHttpOptions());
		return WriteFactory.createWrite(this.options.getWriteOptions(), client);
	}

}
