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

import io.lindb.client.internal.HttpClient;
import io.lindb.client.model.ClusterConfig;
import io.lindb.client.model.Database;
import io.lindb.client.model.ExecParams;
import io.lindb.client.util.JsonUtil;

/**
 * MetadataManagerImpl implements {@link MetadataManager} interface
 */
public class MetadataManagerImpl implements MetadataManager {
	private final HttpClient client;
	private final String url;

	/**
	 * Create metadata manager instance.
	 * 
	 * @param url    url
	 * @param client http client
	 */
	public MetadataManagerImpl(String url, HttpClient client) {
		this.url = url;
		this.client = client;
	}

	/*
	 * Create storage cluster.
	 *
	 * @see io.lindb.client.api.MetadataManager#createStorage(io.lindb.client.model.
	 * ClusterConfig)
	 * 
	 * @param config cluster config
	 * 
	 * @throws Exception throws exception when fail
	 */
	@Override
	public void createStorage(ClusterConfig config) throws Exception {
		String ql = String.format("create storage %s", JsonUtil.toString(config));

		ExecParams params = new ExecParams();
		params.setSql(ql);
		this.client.put(this.url, params, String.class);
	}

	/*
	 * Create database.
	 * 
	 * @see
	 * io.lindb.client.api.MetadataManager#createDatabase(io.lindb.client.model.
	 * Database)
	 * 
	 * @param database database config
	 * 
	 * @throws Exception throws exception when fail
	 */
	@Override
	public void createDatabase(Database database) throws Exception {
		String ql = String.format("create database %s", JsonUtil.toString(database));

		ExecParams params = new ExecParams();
		params.setSql(ql);
		this.client.put(this.url, params, String.class);
	}

	/*
	 * Drop database.
	 * 
	 * @see io.lindb.client.api.MetadataManager#dropDatabase(java.lang.String)
	 * 
	 * @param name database name
	 * 
	 * @throws Exception throws exception when fail
	 */
	@Override
	public void dropDatabase(String name) throws Exception {
		String ql = String.format("drop database %s", name);

		ExecParams params = new ExecParams();
		params.setSql(ql);
		this.client.put(this.url, params, String.class);
	}

}
