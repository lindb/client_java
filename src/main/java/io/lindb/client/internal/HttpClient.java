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

import java.io.IOException;

import io.lindb.client.util.JsonUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http write client.
 */
public class HttpClient {
	/**
	 * http user agent information
	 */
	protected final static String USER_AGENT = String.format("lindb-client-java/%s (%s; %s) Java/%s", "0.1.0",
			System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("java.version"));
	/**
	 * Json media type
	 */
	public final static MediaType MEDIT_JSON = MediaType.parse("application/json");

	private final OkHttpClient client;

	/**
	 * Create http clinet instance.
	 * 
	 * @param client {@link OkHttpClient} http client
	 */
	public HttpClient(OkHttpClient client) {
		this.client = client;
	}

	/**
	 * Do http put request.
	 * 
	 * @param <T>    result type
	 * @param url    url
	 * @param params params
	 * @param clazz  result class
	 * @return result object
	 * @throws Exception throws exception when request failure
	 */
	public <T> T put(String url, Object params, Class<T> clazz) throws Exception {
		Request.Builder rb = new Request.Builder()
				.header("User-Agent", USER_AGENT)
				.url(url);
		Request request = rb.put(RequestBody.create(JsonUtil.toString(params), MEDIT_JSON)).build();
		Call call = this.client.newCall(request);
		try (Response response = call.execute()) {
			try {
				String respBody = response.body().string();
				if (response.code() < 400) {
					return JsonUtil.toObject(respBody, clazz);
				}
				throw new IOException(respBody);
			} finally {
				response.body().close();
			}
		}
	}
}
