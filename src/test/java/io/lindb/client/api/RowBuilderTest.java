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
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RowBuilderTest {
	@Test
	public void build() {
		// not tags
		Point point = Point.builder("cpu").namespace("ns").addSum("sum", 1.0).build();
		RowBuilder builder = new RowBuilder();
		byte[] data = builder.build(point, null);
		assertTrue(data.length > 0);
		builder.reset();

		// test reset data
		byte[] data2 = builder.build(point, null);
		assertTrue(data2.length == data.length);
		assertEquals(new String(data), new String(data2));
		builder.reset();

		// has tags
		point = Point.builder("cpu").addTag("key", "value").addSum("sum", 1.0).build();
		data = builder.build(point, null);
		assertTrue(data.length > 0);
		builder.reset();

		Map<String, String> defaultTags = new HashMap<>();
		defaultTags.put("key1", "value2");
		point = Point.builder("cpu").addTag("key", "value")
				.addHistogram(1.0, 1.0, 1.0, 1.0, new double[] { 1.0 }, new double[] { 1.2 })
				.build();
		data2 = builder.build(point, defaultTags);
		assertTrue(data2.length > 0);
		assertTrue(data.length < data2.length);
		builder.reset();

		// no tags but with default tags
		point = Point.builder("cpu")
				.addHistogram(1.0, 1.0, 1.0, 1.0, new double[] { 1.0 }, new double[] { 1.2 })
				.build();
		data2 = builder.build(point, defaultTags);
		assertTrue(data2.length > 0);
		builder.reset();
	}
}
