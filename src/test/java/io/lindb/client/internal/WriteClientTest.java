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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import io.lindb.client.Constants;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class WriteClientTest {

	@Test
	public void writeMetric() throws IOException {
		// Create a MockWebServer.
		MockWebServer server = new MockWebServer();
		try {
			server.enqueue(new MockResponse().setResponseCode(204));
			server.enqueue(new MockResponse().setBody("write failure").setResponseCode(500));
			// Start the server.
			server.start();

			HttpUrl baseUrl = server.url(Constants.WRITE_API);
			WriteClient client = new WriteClient(baseUrl.toString(), HttpOptions.builder().build());
			// write success
			assertTrue(client.writeMetric("data".getBytes(), true));
			// write failure
			assertFalse(client.writeMetric("data".getBytes(), false));
		} finally {
			server.close();
		}
	}
}
