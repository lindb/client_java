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
package io.lindb.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import io.lindb.client.api.WriteOptions;
import io.lindb.client.internal.HttpOptions;

public class OptionsTest {

	@Test
	public void invalidOptions() {
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().readTimeout(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().connectTimeout(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().writeTimeout(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().batchSize(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().retryQueue(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().flushInterval(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().maxRetries(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().addDefaultTag(null, null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().addDefaultTag("", null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().addDefaultTag("key", null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().addDefaultTag("key", null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().retryQueue(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().batchQueue(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Options.builder().sendQueue(-1);
		});
	}

	@Test
	public void options() {
		// test default options
		Options options = Options.builder().build();
		HttpOptions httpOptions = options.getHttpOptions();
		assertEquals(HttpOptions.DEFAULT_TIMEOUT, httpOptions.getConnectTimeout());
		assertEquals(HttpOptions.DEFAULT_TIMEOUT, httpOptions.getReadTimeout());
		assertEquals(HttpOptions.DEFAULT_TIMEOUT, httpOptions.getWriteTimeout());

		WriteOptions writeOptions = options.getWriteOptions();
		assertEquals(WriteOptions.DEFAULT_BATCH_SIZE, writeOptions.getBatchSize());
		assertEquals(WriteOptions.DEFAULT_MAX_RETRIES, writeOptions.getMaxRetries());
		assertEquals(WriteOptions.DEFAULT_USE_GZIP, writeOptions.isUseGZip());
		assertEquals(WriteOptions.DEFAULT_FLUSH_INTERVAL, writeOptions.getFlushInterval());
		assertEquals(WriteOptions.DEFAULT_RETRY_QUEUE, writeOptions.getRetryQueue());
		assertEquals(WriteOptions.DEFAULT_BATCH_QUEUE, writeOptions.getBatchQueue());
		assertEquals(WriteOptions.DEFAULT_SEND_QUEUE, writeOptions.getSendQueue());
		assertNull(writeOptions.getDefaultTags());

		// test options
		options = Options.builder()
				.connectTimeout(1)
				.writeTimeout(2)
				.readTimeout(3)
				.useGZip(false)
				.batchSize(4)
				.maxRetries(5)
				.flushInterval(6)
				.retryQueue(7)
				.batchQueue(8)
				.sendQueue(9)
				.addDefaultTag("key", "value")
				.build();
		httpOptions = options.getHttpOptions();
		assertEquals(1, httpOptions.getConnectTimeout());
		assertEquals(2, httpOptions.getWriteTimeout());
		assertEquals(3, httpOptions.getReadTimeout());

		writeOptions = options.getWriteOptions();
		assertFalse(writeOptions.isUseGZip());
		assertEquals(4, writeOptions.getBatchSize());
		assertEquals(5, writeOptions.getMaxRetries());
		assertEquals(6, writeOptions.getFlushInterval());
		assertEquals(7, writeOptions.getRetryQueue());
		assertEquals(8, writeOptions.getBatchQueue());
		assertEquals(9, writeOptions.getSendQueue());
		assertNotNull(writeOptions.getDefaultTags());
	}
}
