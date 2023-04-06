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

import org.junit.Test;

import io.lindb.client.internal.HttpClient;
import io.lindb.client.internal.HttpOptions;
import io.lindb.client.model.ResultSet;
import okhttp3.mockwebserver.MockWebServer;

public class DataQueryImplTest {
	@Test
	public void metricData() throws Exception {
		try (MockWebServer server = QueryHelper.mockServer(new ResultSet())) {
			HttpClient client = new HttpClient(HttpOptions.builder().build());
			DataQueryImpl query = new DataQueryImpl(server.url("exec").toString(), client);
			assertNotNull(query.dataQuery("test", "select load from cpu"));
			assertNotNull(query.metadataQuery("test", "show fields from cpu"));
		}
	}
}
