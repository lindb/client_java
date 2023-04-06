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
package io.lindb.client.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import io.lindb.client.Constants;
import io.lindb.client.util.JsonUtil;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class HttpClientTest {

	@Test
	public void put() throws Exception {
		// Create a MockWebServer.
		MockWebServer server = new MockWebServer();
		try {
			server.enqueue(new MockResponse().setBody(JsonUtil.toString("ok")).setResponseCode(200));
			server.enqueue(new MockResponse().setBody("write failure").setResponseCode(500));
			// Start the server.
			server.start();

			HttpUrl baseUrl = server.url(Constants.EXEC_API);
			HttpClient client = new HttpClient(HttpOptions.builder().build());
			// put success
			String result = client.put(baseUrl.toString(), "data", String.class);
			assertEquals("ok", result);
			boolean throwEx = false;
			try {
				// put failure
				client.put(baseUrl.toString(), "data", String.class);
				fail();
			} catch (Exception e) {
				throwEx = true;
			}
			assertTrue(throwEx);
		} finally {
			server.close();
		}
	}
}
