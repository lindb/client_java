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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Map;

import org.junit.Test;

public class WriteOptionsTest {
	@Test
	public void invalidOptions() {
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().batchSize(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().retryQueue(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().flushInterval(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().maxRetries(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().addDefaultTag(null, null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().addDefaultTag("", null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().addDefaultTag("key", null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().addDefaultTag("key", null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().retryQueue(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().batchQueue(-1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WriteOptions.builder().sendQueue(-1);
		});
	}

	@Test
	public void options() {
		WriteOptions options = WriteOptions.builder().build();
		assertNull(options.getDefaultTags());

		options = WriteOptions.builder()
				.addDefaultTag("key1", "value1")
				.addDefaultTag("key2", "value2")
				.build();
		Map<String, String> tags = options.getDefaultTags();
		assertEquals("value1", tags.get("key1"));
		assertEquals("value2", tags.get("key2"));
		assertEquals(2, tags.size());
	}
}
