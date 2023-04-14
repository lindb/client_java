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
import io.lindb.client.model.ClusterConfig;
import io.lindb.client.model.Database;
import io.lindb.client.model.DatabaseOption;
import io.lindb.client.model.Interval;
import io.lindb.client.model.RepoState;

public class MetadataManager {

	public static void main(String[] args) throws Exception {
		Options options = Options.builder().build();
		// create LinDB Client with broker endpoint
		Client client = ClientFactory.create("http://localhost:9000", options);
		// create metadata manager
		io.lindb.client.api.MetadataManager manager = client.metadataManager();
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
		// drop database
		manager.dropDatabase("java");
		client.close();
	}
}
