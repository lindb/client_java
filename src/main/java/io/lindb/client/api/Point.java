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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.NotThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lindb.client.util.StringUtils;

/**
 * Metric data point for a time series.
 */
public class Point {
	/**
	 * Data point builder.
	 */
	@NotThreadSafe
	public static class Builder {
		private Point point;

		private Builder() {
		}

		/**
		 * Set the namespace of point.
		 * 
		 * @param namespace
		 * @return builder
		 */
		public Builder namespace(String namespace) {
			this.point.namespace = namespace;
			return this;
		}

		/**
		 * Add tag key/value into the tags of point.
		 * 
		 * @param key
		 * @param value
		 * @return builder
		 */
		public Builder addTag(String key, String value) {
			if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
				// ignore empty key/value
				LOGGER.warn("tag key/value is empty, ignore this tag");
				return this;
			}
			if (this.point.tags == null) {
				this.point.tags = new HashMap<>();
			}
			this.point.tags.put(key, value);
			return this;
		}

		/**
		 * Add tags into the tags of point.
		 * 
		 * @param tags
		 * @return builder
		 */
		public Builder addTags(Map<String, String> tags) {
			if (tags == null || tags.isEmpty()) {
				return this;
			}
			if (this.point.tags == null) {
				this.point.tags = new HashMap<>();
			}
			this.point.tags.putAll(tags);
			return this;
		}

		/**
		 * Add sum simple field with name and value.
		 * 
		 * @param name
		 * @param value
		 * @return builder
		 */
		public Builder addSum(String name, double value) {
			return this.addSimpleField(name, FieldType.Sum, value);
		}

		/**
		 * Add min simple field with name and value.
		 * 
		 * @param name
		 * @param value
		 * @return builder
		 */
		public Builder addMin(String name, double value) {
			return this.addSimpleField(name, FieldType.Min, value);
		}

		/**
		 * Add max simple field with name and value.
		 * 
		 * @param name
		 * @param value
		 * @return builder
		 */
		public Builder addMax(String name, double value) {
			return this.addSimpleField(name, FieldType.Max, value);
		}

		/**
		 * Add last simple field with name and value.
		 * 
		 * @param name
		 * @param value
		 * @return builder
		 */
		public Builder addLast(String name, double value) {
			return this.addSimpleField(name, FieldType.Last, value);
		}

		/**
		 * Add first simple field with name and value.
		 * 
		 * @param name
		 * @param value
		 * @return builder
		 */
		public Builder addFirst(String name, double value) {
			return this.addSimpleField(name, FieldType.First, value);
		}

		/**
		 * Add histogram field with values.
		 * 
		 * @param count
		 * @param sum
		 * @param min
		 * @param max
		 * @param bounds
		 * @param values
		 * @return builder
		 */
		public Builder addHistogram(double count, double sum, double min, double max, double[] bounds,
				double[] values) {
			if (bounds == null || bounds.length == 0 || values == null || values.length == 0) {
				LOGGER.warn("histogram bounds/values cannot be empty");
				return this;
			}
			if (values.length != bounds.length) {
				LOGGER.warn("histogram bounds/values length not equals");
				return this;
			}
			if (this.point.compoundField != null || this.point.simpleFields != null) {
				LOGGER.warn("point has field, cannot add compound field");
				return this;
			}
			this.point.compoundField = new CompoundField(count, sum, min, max, bounds, values);
			return this;
		}

		/**
		 * Return the point with setting.
		 * 
		 * @return point
		 */
		public Point build() {
			return this.point;
		}

		private Builder addSimpleField(String name, FieldType type, double value) {
			if (StringUtils.isEmpty(name)) {
				LOGGER.warn("field name cannot be empty");
				return this;
			}
			this.addSimpleField(new SimpleField(name, type, value));
			return this;
		}

		private Builder addSimpleField(Field field) {
			if (this.point.compoundField != null) {
				LOGGER.warn("point has compound field, cannot add simple field");
				return this;
			}
			if (this.point.simpleFields == null) {
				this.point.simpleFields = new ArrayList<>();
			}
			this.point.simpleFields.add(field);
			return this;
		}

		/**
		 * Add field just for testing.
		 * 
		 * @param field {@link Field}
		 * @return builder
		 */
		protected Builder addField(Field field) {
			this.point.simpleFields = new ArrayList<>();
			this.point.simpleFields.add(field);
			return this;
		}
	}

	private final static Logger LOGGER = LoggerFactory.getLogger(Point.class);

	/**
	 * Create a builder instance with metric name.
	 * 
	 * @param name
	 * @return builder
	 */
	public static Builder builder(String name) {
		return builder(name, System.currentTimeMillis());
	}

	/**
	 * Create a builder instance with metric name and timestamp.
	 * 
	 * @param name
	 * @param timestamp
	 * @return builder
	 */
	public static Builder builder(String name, long timestamp) {
		Builder builder = new Builder();
		builder.point = new Point(name, timestamp);
		return builder;
	}

	private String namespace;
	private String name;
	private long timestamp;
	private Map<String, String> tags;
	private List<Field> simpleFields;
	private CompoundField compoundField;

	private Point(String name, long timestamp) {
		this.name = name;
		this.timestamp = timestamp;
	}

	/**
	 * Return the namespace.
	 * 
	 * @return ns
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Return the metric name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the timestamp(ms).
	 * 
	 * @return timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Return the tags.
	 * 
	 * @return tags
	 */
	public Map<String, String> getTags() {
		return tags;
	}

	/**
	 * Return the simple fiels.
	 * 
	 * @return field
	 */
	public List<Field> getSimpleFields() {
		return simpleFields;
	}

	/**
	 * Return the compound field.
	 * 
	 * @return field
	 */
	public CompoundField getCompoundField() {
		return compoundField;
	}

	/**
	 * Check point if it is valid.
	 * 
	 * @return return true point is valid, else invalid
	 */
	public boolean validate() {
		if (StringUtils.isEmpty(this.name)) {
			return false;
		}
		if (this.simpleFields == null && this.compoundField == null) {
			return false;
		}
		return true;
	}

}
