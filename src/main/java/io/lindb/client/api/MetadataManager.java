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

import io.lindb.client.model.ClusterConfig;
import io.lindb.client.model.Database;

/**
 * Metadata manager interface(storage/database).
 */
public interface MetadataManager {

	/**
	 * Create storage cluster.
	 * 
	 * @param config cluster config
	 * @throws Exception throws exception when fail
	 */
	void createStorage(ClusterConfig config) throws Exception;

	/**
	 * Create database.
	 * 
	 * @param database database config
	 * @throws Exception throws exception when fail
	 */
	void createDatabase(Database database) throws Exception;

	/**
	 * Drop database.
	 * 
	 * @param name database name
	 * @throws Exception throws exception when fail
	 */
	void dropDatabase(String name) throws Exception;
}
