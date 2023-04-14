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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.lindb.client.internal.BaseClientTest;
import io.lindb.client.internal.HttpClient;
import io.lindb.client.model.ClusterConfig;
import io.lindb.client.model.Database;
import io.lindb.client.model.DatabaseOption;
import io.lindb.client.model.Interval;
import io.lindb.client.model.RepoState;
import okhttp3.mockwebserver.MockWebServer;

public class MetadataManagerImplTest extends BaseClientTest {
	private final HttpClient client;

	public MetadataManagerImplTest() {
		this.client = new HttpClient(cli);
	}

	@Test
	public void createStorage() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer("ok")) {
			MetadataManagerImpl manager = new MetadataManagerImpl(server.url("exec").toString(), client);
			ClusterConfig cluster = new ClusterConfig();
			RepoState repo = new RepoState();
			repo.setNamespace("/java-client");
			List<String> endpoints = new ArrayList<>();
			endpoints.add("http://localhost:2379");
			repo.setEndpoints(endpoints);
			repo.setLeaseTTL("10s");
			repo.setDialTimeout("5s");
			repo.setTimeout("5s");
			cluster.setConfig(repo);
			// create storage
			manager.createStorage(cluster);
		}
	}

	@Test
	public void createDatabase() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer("ok")) {
			MetadataManagerImpl manager = new MetadataManagerImpl(server.url("exec").toString(), client);
			Database database = new Database();
			database.setName("java");
			database.setStorage("/lindb-cluster");
			database.setNumOfShard(1);
			database.setReplicaFactor(1);
			DatabaseOption option = new DatabaseOption();
			option.setAhead("1h");
			option.setBehind("1h");
			option.setAutoCreateNS(true);
			Interval interval = new Interval();
			interval.setInterval("10s");
			interval.setRetention("30d");
			List<Interval> intervals = new ArrayList<>();
			intervals.add(interval);
			option.setIntervals(intervals);
			database.setOption(option);
			// create database
			manager.createDatabase(database);
		}
	}

	@Test
	public void dropDatabase() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer("ok")) {
			MetadataManagerImpl manager = new MetadataManagerImpl(server.url("exec").toString(), client);
			// create storage
			manager.dropDatabase("java");
		}
	}
}
