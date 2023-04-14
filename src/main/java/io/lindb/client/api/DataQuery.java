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

import io.lindb.client.model.Metadata;
import io.lindb.client.model.ResultSet;

/**
 * Query metric data/metadata from database by given ql.
 *
 * LinQL ref: https://lindb.io/guide/lin-ql.html#query-data
 */
public interface DataQuery {
	/**
	 * Query time series data from database by given ql.
	 * LinQL ref: https://lindb.io/guide/lin-ql.html#metric-query
	 * Example:
	 * select heap_objects from lindb.runtime.mem group by node
	 * 
	 * @param database database name
	 * @param ql       lin query language
	 * @return result set
	 * @throws Exception throws exception when query failure
	 */
	ResultSet dataQuery(String database, String ql) throws Exception;

	/**
	 * Query metric metadata from database by given ql.
	 * LinQL ref: https://lindb.io/guide/lin-ql.html#metric-meta-query
	 * Example:
	 * show fields from lindb.runtime.mem
	 * 
	 * @param <T>      result type
	 * @param database database name
	 * @param ql       lin query language
	 * @return result set
	 * @throws Exception throws exception when query failure
	 */
	<T> Metadata<T> metadataQuery(String database, String ql) throws Exception;
}
