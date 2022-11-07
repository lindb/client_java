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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class PointTest {

	@Test
	public void invalidPoint() {
		Point point = Point.builder("name").build();
		assertFalse(point.validate());

		point = Point.builder("").build();
		assertFalse(point.validate());

		point = Point.builder("name").addMin("", 1.0).build();
		assertFalse(point.validate());

		point = Point.builder("name")
				.addHistogram(1.0, 1.0, 1, 1, null, new double[] { 1 })
				.build();
		assertFalse(point.validate());
		point = Point.builder("name")
				.addHistogram(1.0, 1.0, 1, 1, new double[] {}, new double[] { 1 })
				.build();
		assertFalse(point.validate());
		point = Point.builder("name")
				.addHistogram(1.0, 1.0, 1, 1, new double[] { 1 }, null)
				.build();
		assertFalse(point.validate());
		point = Point.builder("name")
				.addHistogram(1.0, 1.0, 1, 1, new double[] { 1 }, new double[] {})
				.build();
		assertFalse(point.validate());
		point = Point.builder("name")
				.addHistogram(1.0, 1.0, 1, 1, new double[] { 1, 2 }, new double[] { 1 })
				.build();
		assertFalse(point.validate());
	}

	@Test
	public void buildPoint() {
		// simple field
		Point point = Point.builder("name").namespace("ns")
				.addTag(null, null)
				.addTag("key", null)
				.addTag("key1", "value1")
				.addTag("key2", "value2")
				.addSum("sum", 1.0)
				.addMin("min", 1.0)
				.addMax("max", 1.0)
				.addLast("last", 1.0)
				.addFirst("first", 1.0)
				.addHistogram(1.0, 1.0, 1, 1, new double[] { 1 }, new double[] { 1 }) // ignore histogram, because has
																						// simple field
				.build();
		assertEquals("ns", point.getNamespace());
		assertEquals("name", point.getName());
		assertTrue(point.getTimestamp() <= System.currentTimeMillis());
		assertNotNull(point);
		assertTrue(point.validate());
		assertEquals(5, point.getSimpleFields().size());
		assertNull(point.getCompoundField());
		assertEquals(2, point.getTags().size());
		assertEquals("value1", point.getTags().get("key1"));
		assertEquals("value2", point.getTags().get("key2"));

		// histogram
		Map<String, String> tags1 = new HashMap<>();
		tags1.put("key1", "value1");
		Map<String, String> tags2 = new HashMap<>();
		tags2.put("key2", "value2");
		point = Point.builder("name").namespace("ns")
				.addTags(null)
				.addTags(new HashMap<>())
				.addTags(tags1)
				.addTags(tags2)
				.addHistogram(1.0, 1.0, 1, 1, new double[] { 1 }, new double[] { 1 })
				.addHistogram(2.0, 3.0, 1, 1, new double[] { 1 }, new double[] { 1 }) // ignore histogram, because has
																						// histogram
				.addSum("sum", 1.0) // ignore sum, because has histogram
				.build();
		assertTrue(point.validate());
		assertNull(point.getSimpleFields());
		assertEquals(2, point.getTags().size());
		assertEquals("value1", point.getTags().get("key1"));
		assertEquals("value2", point.getTags().get("key2"));
		assertNotNull(point.getCompoundField());
		assertEquals(1.0, point.getCompoundField().getCount(), 0);
	}
}
