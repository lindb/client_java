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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.lindb.client.internal.HttpClient;
import io.lindb.client.model.DataFamilyState;
import io.lindb.client.model.Database;
import io.lindb.client.model.FamilyLogReplicaState;
import io.lindb.client.model.Master;
import io.lindb.client.model.RuntimeMetric;
import io.lindb.client.model.StatelessNode;
import io.lindb.client.model.StorageCluster;
import io.lindb.client.model.StorageState;

/**
 * StateQueryImpl implements {@link StateQuery} interface
 */
public class StateQueryImpl extends BaseQuery implements StateQuery {

	/**
	 * Create state query instance.
	 * 
	 * @param url    url
	 * @param client http client
	 */
	public StateQueryImpl(String url, HttpClient client) {
		super(url, client);
	}

	@Override
	public <T> T query(String ql, Class<T> clazz) throws Exception {
		return super.query(ql, clazz);
	}

	@Override
	public Master master() throws Exception {
		return query("show master", Master.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StatelessNode> brokerAliveNodes() throws Exception {
		return query("show broker alive", List.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StorageState> aliveStorages() throws Exception {
		return query("show storage alive", List.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> databaseNames() throws Exception {
		return query("show databases", List.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, List<RuntimeMetric>> brokerMetrics(List<String> metricNames) throws Exception {
		if (metricNames == null || metricNames.isEmpty()) {
			return new HashMap<>();
		}
		String joined = metricNames.stream().map(item -> String.format("'%s'", item))
				.collect(Collectors.joining(","));
		String ql = String.format("show broker metric where metric in (%s)", joined);
		return query(ql, Map.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, List<RuntimeMetric>> stroageMetrics(String storage, List<String> metricNames) throws Exception {
		if (metricNames == null || metricNames.isEmpty()) {
			return new HashMap<>();
		}
		String joined = metricNames.stream().map(item -> String.format("'%s'", item))
				.collect(Collectors.joining(","));
		String ql = String.format("show storage metric where storage='%s' and metric in (%s)", storage, joined);
		return query(ql, Map.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Database> databases() throws Exception {
		return query("show schemas", List.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StorageCluster> storages() throws Exception {
		return query("show storages", List.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, List<FamilyLogReplicaState>> replicationState(String storage, String database) throws Exception {
		return query(String.format("show replication where storage='%s' and database='%s'", storage, database),
				Map.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, List<DataFamilyState>> dataFamilyState(String storage, String database) throws Exception {
		return query(String.format("show memory database where storage='%s' and database='%s'", storage, database),
				Map.class);
	}

}
