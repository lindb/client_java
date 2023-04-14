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
package io.lindb.client.example;

import java.util.ArrayList;
import java.util.List;

import io.lindb.client.Client;
import io.lindb.client.ClientFactory;
import io.lindb.client.Options;
import io.lindb.client.model.Master;

public class StateQuery {
	public static void main(String[] args) throws Exception {
		Options options = Options.builder().build();
		// create LinDB Client with broker endpoint
		Client client = ClientFactory.create("http://localhost:9000", options);
		// create state query api
		io.lindb.client.api.StateQuery query = client.stateQuery();
		System.out.println("master: =>" + query.master());
		System.out.println("databases: =>" + query.databases());
		System.out.println("database names: =>" + query.databaseNames());
		System.out.println("broker alive nodes: =>" + query.brokerAliveNodes());
		System.out.println("alive storages: =>" + query.aliveStorages());
		System.out.println("storages: =>" + query.storages());
		List<String> metrics = new ArrayList<>();
		metrics.add("lindb.monitor.system.cpu_stat");
		metrics.add("lindb.monitor.system.mem_stat");
		System.out.println("broker runtime metrics: =>" + query.brokerMetrics(metrics));
		System.out.println("storage runtime metrics: =>" + query.stroageMetrics("/lindb-cluster", metrics));
		System.out.println("replication state: =>" + query.dataFamilyState("/lindb-cluster", "_internal"));
		System.out.println("data family state: =>" + query.replicationState("/lindb-cluster", "_internal"));
		System.out.println("common ql: =>" + query.query("show master", Master.class));
		client.close();
	}
}
