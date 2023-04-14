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

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.lindb.client.internal.BaseClientTest;
import io.lindb.client.internal.WriteClient;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class BlockingWriteImplTest extends BaseClientTest {

	@Test
	public void write() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {

			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				return new MockResponse().setResponseCode(200);
			}
		};
		server.setDispatcher(dispatcher);
		WriteClient client = new WriteClient(server.url("/test").toString(), cli);

		WriteOptions options = WriteOptions.builder().build();
		Point point = Point.builder("test").addLast("last", 1.0).build();
		List<Point> points = new ArrayList<>();
		points.add(point);

		BlockingWriteImpl write = new BlockingWriteImpl(options, client);
		try {
			write.write(points);
		} finally {
			server.close();
		}
	}

	@Test
	public void writeFailure() throws Exception {
		MockWebServer server = new MockWebServer();
		final Dispatcher dispatcher = new Dispatcher() {

			@Override
			public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
				return new MockResponse().setResponseCode(500);
			}
		};
		server.setDispatcher(dispatcher);
		WriteClient client = new WriteClient(server.url("/test").toString(), cli);

		WriteOptions options = WriteOptions.builder().useGZip(false).build();
		Point point = Point.builder("test").addLast("last", 1.0).build();
		List<Point> points = new ArrayList<>();
		points.add(point);

		BlockingWriteImpl write = new BlockingWriteImpl(options, client);
		try {
			assertThrows(IOException.class, () -> {
				write.write(points);
			});
		} finally {
			server.close();
		}
	}
}
