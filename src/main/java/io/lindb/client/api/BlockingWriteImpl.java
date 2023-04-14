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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.annotation.concurrent.ThreadSafe;

import io.lindb.client.internal.WriteClient;

/**
 * Implement a blocking write api.
 */
@ThreadSafe
public class BlockingWriteImpl implements BlockingWrite {
	private final boolean useGZip;
	private final Map<String, String> defaultTags;

	private RowBuilder builder;
	private ByteArrayOutputStream buffer;
	private WriteClient client;
	private ByteArrayOutputStream outputStream; // for gzip compress

	/**
	 * Create a write api instance with options and http client.
	 * 
	 * @param options write options
	 * @param client  http write client
	 * @throws IOException create error
	 */
	protected BlockingWriteImpl(WriteOptions options, WriteClient client) throws IOException {
		this.useGZip = options.isUseGZip();

		this.outputStream = new ByteArrayOutputStream();
		this.defaultTags = options.getDefaultTags();

		this.client = client;
		this.buffer = new ByteArrayOutputStream();
		this.builder = new RowBuilder();
	}

	/**
	 * Write metric points.
	 * 
	 * @param points metric data points
	 * @throws IOException throws {@link IOException} when fail
	 */
	@Override
	public void write(List<Point> points) throws IOException {
		try {
			for (Point point : points) {
				byte[] data = builder.build(point, defaultTags);
				buffer.write(data);
				builder.reset();
			}
			byte[] data = buffer.toByteArray();

			if (useGZip) {
				GZIPOutputStream gzip = new GZIPOutputStream(outputStream);
				// compress using gzip
				gzip.write(data);
				gzip.close();
				data = outputStream.toByteArray();
				outputStream.reset();
			}

			client.sendMetric(data, useGZip);
		} finally {
			// need reset resource
			buffer.reset();
			builder.reset();
			outputStream.reset();
		}
	}
}
