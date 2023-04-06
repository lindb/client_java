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
import io.lindb.client.model.ExecParams;

/**
 * Query metric data/metadata/state by given ql.
 */
public abstract class BaseQuery {
	private final HttpClient client;
	private final String url;

	/**
	 * Create base query instance.
	 */
	protected BaseQuery(String url, HttpClient client) {
		this.url = url;
		this.client = client;
	}

	/**
	 * Query data by given ql.
	 * 
	 * @param <T>   result type
	 * @param ql    lin query language
	 * @param clazz result class
	 * @return result set
	 * @throws Exception throw exception when query failure
	 */
	public <T> T query(String ql, Class<T> clazz) throws Exception {
		return query("", ql, clazz);
	}

	/**
	 * Query data by given database/ql.
	 * 
	 * @param <T>      result type
	 * @param database database name
	 * @param ql       lin query language
	 * @param clazz    result class
	 * @return result set
	 * @throws Exception throw exception when query failure
	 */
	public <T> T query(String database, String ql, Class<T> clazz) throws Exception {
		ExecParams params = new ExecParams();
		params.setDatabase(database);
		params.setSql(ql);
		return this.client.put(this.url, params, clazz);
	}

}
