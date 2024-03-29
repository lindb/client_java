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
import java.util.concurrent.TimeUnit;

import io.lindb.client.api.BlockingWrite;
import io.lindb.client.api.DataQuery;
import io.lindb.client.api.DataQueryImpl;
import io.lindb.client.api.EventListener;
import io.lindb.client.api.MetadataManager;
import io.lindb.client.api.MetadataManagerImpl;
import io.lindb.client.api.StateQuery;
import io.lindb.client.api.StateQueryImpl;
import io.lindb.client.api.Write;
import io.lindb.client.api.WriteFactory;
import io.lindb.client.internal.HttpClient;
import io.lindb.client.internal.HttpOptions;
import io.lindb.client.internal.WriteClient;
import okhttp3.OkHttpClient;

/**
 * ClientImpl implements {@link Client} interface
 */
public class ClientImpl implements Client {
	private final String brokerEndpoint;
	private final Options options;
	private final OkHttpClient client;

	/**
	 * Create LinDB client instance
	 * 
	 * @param brokerEndpoint broker endpoint {@link String}
	 * @param options        write/http options {@link Options}
	 */
	protected ClientImpl(String brokerEndpoint, Options options) {
		this.brokerEndpoint = brokerEndpoint;
		this.options = options;
		HttpOptions httpOptions = options.getHttpOptions();
		this.client = new OkHttpClient.Builder().connectTimeout(httpOptions.getConnectTimeout(), TimeUnit.SECONDS)
				.writeTimeout(httpOptions.getWriteTimeout(), TimeUnit.SECONDS)
				.readTimeout(httpOptions.getReadTimeout(), TimeUnit.SECONDS).build();
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
	 * @throws {@link IOException} if create error
	 *
	 */
	@Override
	public Write write(String database) throws IOException {
		return write(database, null);
	}

	/**
	 * Create an async write client by given database name
	 * 
	 * @see io.lindb.client.Client#write(java.lang.String)
	 * 
	 * @param database database name {@link String}
	 * @param listener the listener to listen events
	 * @return write client {@link Write}
	 * @throws IOException if create error
	 */
	@Override
	public Write write(String database, EventListener listener) throws IOException {
		String url = String.format("%s%s?db=%s", this.brokerEndpoint, Constants.WRITE_API, database);
		WriteClient client = new WriteClient(url, this.client);
		return WriteFactory.createWrite(this.options.getWriteOptions(), client, listener);
	}

	/**
	 * Create a blocking write client by given database name
	 * 
	 * @see io.lindb.client.Client#blockingWrite(java.lang.String)
	 * 
	 * @param database database name {@link String}
	 * @return write client {@link Write}
	 * @throws IOException if create write error
	 */
	@Override
	public BlockingWrite blockingWrite(String database) throws IOException {
		String url = String.format("%s%s?db=%s", this.brokerEndpoint, Constants.WRITE_API, database);
		WriteClient client = new WriteClient(url, this.client);
		return WriteFactory.createBlockingWrite(this.options.getWriteOptions(), client);
	}

	/**
	 * Create metric data query client.
	 * 
	 * @see io.lindb.client.Client#dataQuery()
	 * 
	 * @return metric data client {@link DataQuery}
	 */
	@Override
	public DataQuery dataQuery() {
		String url = String.format("%s%s", this.brokerEndpoint, Constants.EXEC_API);
		HttpClient client = new HttpClient(this.client);
		return new DataQueryImpl(url, client);
	}

	/**
	 * Create system state query client.
	 * 
	 * @see io.lindb.client.Client#stateQuery()
	 * 
	 * @return state query client {@link StateQuery}
	 */
	@Override
	public StateQuery stateQuery() {
		String url = String.format("%s%s", this.brokerEndpoint, Constants.EXEC_API);
		HttpClient client = new HttpClient(this.client);
		return new StateQueryImpl(url, client);
	}

	/**
	 * Create metadata manager client.
	 * 
	 * @see io.lindb.client.Client#metadataManager()
	 * 
	 * @return metadata manager client {@link MetadataManager}
	 */
	@Override
	public MetadataManager metadataManager() {
		String url = String.format("%s%s", this.brokerEndpoint, Constants.EXEC_API);
		HttpClient client = new HttpClient(this.client);
		return new MetadataManagerImpl(url, client);
	}

	@Override
	public void close() throws IOException {
		this.client.dispatcher().cancelAll();
		this.client.dispatcher().executorService().shutdown();
		this.client.connectionPool().evictAll();
	}
}
