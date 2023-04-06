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
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http write client.
 */
public class WriteClient {
	private final static Logger LOGGER = LoggerFactory.getLogger(WriteClient.class);
	private final static MediaType MEDIT_FLAT = MediaType.parse("application/flatbuffer");

	private OkHttpClient client;
	private String url;

	/**
	 * Create http write clinet instance.
	 * 
	 * @param url     write url path
	 * @param options {@link HttpOptions}
	 */
	public WriteClient(String url, HttpOptions options) {
		this.url = url;
		client = new OkHttpClient.Builder().connectTimeout(options.getConnectTimeout(), TimeUnit.SECONDS)
				.writeTimeout(options.getWriteTimeout(), TimeUnit.SECONDS)
				.readTimeout(options.getReadTimeout(), TimeUnit.SECONDS).build();
	}

	/**
	 * Write metric point data.
	 * 
	 * @param data     write point data
	 * @param compress if compress point data
	 * @return if write successfully
	 * @throws IOException when send error
	 */
	public boolean writeMetric(byte[] data, boolean compress) throws IOException {
		Request.Builder rb = new Request.Builder()
				.header("User-Agent", HttpClient.USER_AGENT)
				.url(url);
		if (compress) {
			rb.header("Content-Encoding", "gzip");
		}
		Request request = rb.put(RequestBody.create(data, MEDIT_FLAT)).build();
		Call call = this.client.newCall(request);
		try (Response response = call.execute()) {
			if (response.isSuccessful()) {
				return true;
			}
			LOGGER.warn("write metric failure, error msg: {}", response.body().string());
			return false;
		}
	}
}
