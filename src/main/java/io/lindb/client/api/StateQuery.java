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
import java.util.Map;

import io.lindb.client.model.DataFamilyState;
import io.lindb.client.model.Database;
import io.lindb.client.model.FamilyLogReplicaState;
import io.lindb.client.model.Master;
import io.lindb.client.model.RuntimeMetric;
import io.lindb.client.model.StatelessNode;
import io.lindb.client.model.StorageCluster;
import io.lindb.client.model.StorageState;

/**
 * Query state(storage/database/replication etc.) by given ql.
 *
 * LinQL ref: https://lindb.io/guide/lin-ql.html#status-query
 */
public interface StateQuery {
	/**
	 * Return current master.
	 * 
	 * @return master
	 * @throws Exception throws exception when fail
	 */
	Master master() throws Exception;

	/**
	 * Return all database schemas.
	 * 
	 * @return database list
	 * @throws Exception throws exception when fail
	 */
	List<Database> databases() throws Exception;

	/**
	 * Return all database names.
	 * 
	 * @return database names
	 * @throws Exception throws exception when fail
	 */
	List<String> databaseNames() throws Exception;

	/**
	 * Return broker's alive nodes.
	 * 
	 * @return node list
	 * @throws Exception throws exception when fail
	 */
	List<StatelessNode> brokerAliveNodes() throws Exception;

	/**
	 * Return alive storage clusters.
	 * 
	 * @return storage list
	 * @throws Exception throws exception when fail
	 */
	List<StorageState> aliveStorages() throws Exception;

	/**
	 * Return all storages.
	 * 
	 * @return storage list
	 * @throws Exception throws exception when fail
	 */
	List<StorageCluster> storages() throws Exception;

	/**
	 * Return broker's runtime metrics.
	 * 
	 * @param metricNames metric names
	 * @return runtime metric list
	 * @throws Exception throws exception when fail
	 */
	Map<String, List<RuntimeMetric>> brokerMetrics(List<String> metricNames) throws Exception;

	/**
	 * Return storage's runtime metrics.
	 * 
	 * @param storage     storage
	 * @param metricNames metric names
	 * @return runtime metric list
	 * @throws Exception throws exception when fail
	 */
	Map<String, List<RuntimeMetric>> stroageMetrics(String storage, List<String> metricNames) throws Exception;

	/**
	 * Return wal replica state.
	 * 
	 * @param storage  storage
	 * @param database database name
	 * @return replica state
	 * @throws Exception throws exception when fail
	 */
	Map<String, List<FamilyLogReplicaState>> replicationState(String storage, String database) throws Exception;

	/**
	 * Return data family write state.
	 * 
	 * @param storage  storage
	 * @param database database name
	 * @return data family write state
	 * @throws Exception throws exception when fail
	 */
	Map<String, List<DataFamilyState>> dataFamilyState(String storage, String database) throws Exception;

	/**
	 * Return state by given ql.
	 * 
	 * @param <T>   result type
	 * @param ql    lin query language
	 * @param clazz result class
	 * @return result set
	 * @throws Exception throws exception when fail
	 */
	<T> T query(String ql, Class<T> clazz) throws Exception;
}
