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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import io.lindb.client.internal.HttpClient;
import io.lindb.client.internal.HttpOptions;
import io.lindb.client.model.Master;
import okhttp3.mockwebserver.MockWebServer;

public class StateQueryImplTest {
	private final HttpClient client;

	public StateQueryImplTest() {
		this.client = new HttpClient(HttpOptions.builder().build());
	}

	@Test
	public void master() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new Master())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			assertNotNull(query.master());
			assertNotNull(query.query("show master", Master.class));
		}
	}

	@Test
	public void databases() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new ArrayList<>())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			assertNotNull(query.databases());
		}
	}

	@Test
	public void databaseNames() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new ArrayList<>())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			assertNotNull(query.databaseNames());
		}
	}

	@Test
	public void brokerAliveNodes() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new ArrayList<>())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			assertNotNull(query.brokerAliveNodes());
		}
	}

	@Test
	public void aliveStorages() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new ArrayList<>())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			assertNotNull(query.aliveStorages());
		}
	}

	@Test
	public void storages() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new ArrayList<>())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			assertNotNull(query.storages());
		}
	}

	@Test
	public void replicationState() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new HashMap<>())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			assertNotNull(query.replicationState("storage", "db"));
		}
	}

	@Test
	public void dataFamilyState() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new HashMap<>())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			assertNotNull(query.dataFamilyState("storage", "db"));
		}
	}

	@Test
	public void brokerMetrics() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new HashMap<>())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			List<String> metrics = new ArrayList<>();
			assertNotNull(query.brokerMetrics(null));
			assertNotNull(query.brokerMetrics(metrics));
			metrics.add("lindb.monitor.system.cpu_stat");
			metrics.add("lindb.monitor.system.mem_stat");
			assertNotNull(query.brokerMetrics(metrics));
		}
	}

	@Test
	public void storageMetrics() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new HashMap<>())) {
			StateQueryImpl query = new StateQueryImpl(server.url("exec").toString(), client);
			List<String> metrics = new ArrayList<>();
			assertNotNull(query.stroageMetrics("storage", null));
			assertNotNull(query.stroageMetrics("storage", metrics));
			metrics.add("lindb.monitor.system.cpu_stat");
			metrics.add("lindb.monitor.system.mem_stat");
			assertNotNull(query.stroageMetrics("storage", metrics));
		}
	}
}
