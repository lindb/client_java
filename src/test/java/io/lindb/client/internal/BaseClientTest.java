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

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import okhttp3.OkHttpClient;

public class BaseClientTest {
	public static OkHttpClient cli;

	@BeforeClass
	public static void setUp() throws Exception {
		HttpOptions httpOptions = HttpOptions.builder().build();

		cli = new OkHttpClient.Builder().connectTimeout(httpOptions.getConnectTimeout(), TimeUnit.SECONDS)
				.writeTimeout(httpOptions.getWriteTimeout(), TimeUnit.SECONDS)
				.readTimeout(httpOptions.getReadTimeout(), TimeUnit.SECONDS).build();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		cli.dispatcher().executorService().shutdown();
		cli.connectionPool().evictAll();
	}
}
