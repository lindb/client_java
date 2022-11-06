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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.flatbuffers.FlatBufferBuilder;

import io.lindb.client.Constants;
import io.lindb.client.flat.metrics.v1.CompoundField;
import io.lindb.client.flat.metrics.v1.KeyValue;
import io.lindb.client.flat.metrics.v1.Metric;
import io.lindb.client.flat.metrics.v1.SimpleField;
import io.lindb.client.util.StringUtils;

/**
 * Row builder using flat buffer.
 */
public class RowBuilder {
	private FlatBufferBuilder fb;

	/**
	 * Create a row builder instance.
	 */
	public RowBuilder() {
		this.fb = new FlatBufferBuilder();
	}

	/**
	 * Write point into flat buffer then returns binary value of point.
	 * 
	 * @param point       metric data point
	 * @param defaultTags default tags for metric
	 * @return the data of point
	 */
	public byte[] build(final Point point, Map<String, String> defaultTags) {
		// metric name
		int nameOffset = this.fb.createString(point.getName());

		// namespace
		String ns = point.getNamespace();
		if (StringUtils.isEmpty(ns)) {
			ns = Constants.DEFAULT_NAMESPACE;
		}
		int namespaceOffset = this.fb.createString(ns);

		// tags
		Map<String, String> tags = point.getTags();
		int size = 0;
		if (tags != null) {
			size = tags.size();
		}
		if (defaultTags != null) {
			size += defaultTags.size();
		}
		int[] offsets = null;
		int tagOffset = 0;
		if (size != 0) {

			offsets = new int[size];
			int offset = 0;
			if (tags != null) {
				addTags(tags, offsets, 0);
				offset += tags.size();
			}
			if (defaultTags != null) {
				addTags(defaultTags, offsets, offset);
			}

			tagOffset = Metric.createKeyValuesVector(this.fb, offsets);
		}

		// simple fields
		List<Field> simpleFields = point.getSimpleFields();
		int[] simpleFieldsOffsets = null;
		int simpleFieldOffset = 0;
		if (simpleFields != null) {
			simpleFieldsOffsets = new int[simpleFields.size()];
			int idx = 0;
			for (Field field : simpleFields) {
				simpleFieldsOffsets[idx] = field.write(this);
				idx++;
			}
			simpleFieldOffset = this.fb.createVectorOfTables(simpleFieldsOffsets);
		}

		// compound field
		io.lindb.client.api.CompoundField compoundField = point.getCompoundField();
		int compoundFieldOffset = 0;
		if (compoundField != null) {
			compoundFieldOffset = compoundField.write(this);
		}

		// start write metric data
		// must not be nested object: https://github.com/google/flatbuffers/issues/4029
		Metric.startMetric(this.fb);
		Metric.addNamespace(this.fb, namespaceOffset);
		Metric.addName(this.fb, nameOffset);
		Metric.addTimestamp(this.fb, point.getTimestamp());

		// write tags
		if (offsets != null) {
			Metric.addKeyValues(this.fb, tagOffset);
		}
		Metric.addHash(this.fb, 1000);
		// write simple fields
		if (simpleFieldsOffsets != null) {
			Metric.addSimpleFields(this.fb, simpleFieldOffset);
		}
		// write compound field
		if (compoundField != null) {
			Metric.addCompoundField(this.fb, compoundFieldOffset);
		}

		int end = Metric.endMetric(this.fb);
		// signal to the builder finish write metric.
		this.fb.finishSizePrefixed(end);

		return this.fb.sizedByteArray();
	}

	/**
	 * Write simple field into flat buffer.
	 * 
	 * @param name  field name
	 * @param type  field type
	 * @param value field value
	 * @return field offset
	 */
	public int addSimpleField(String name, FieldType type, double value) {
		int nameOffset = this.fb.createString(name);
		SimpleField.startSimpleField(this.fb);
		SimpleField.addName(this.fb, nameOffset);
		SimpleField.addType(this.fb, type.value());
		SimpleField.addValue(this.fb, value);

		return SimpleField.endSimpleField(this.fb);
	}

	/**
	 * Write compound field into flat buffer.
	 * 
	 * @param count  count value
	 * @param sum    sum value
	 * @param min    min value
	 * @param max    max value
	 * @param bounds bound array
	 * @param values value array
	 * @return field offset
	 */
	public int addCompoundField(double count, double sum, double min, double max, double[] bounds, double[] values) {
		CompoundField.startExplicitBoundsVector(this.fb, bounds.length);
		for (double bound : bounds) {
			this.fb.addDouble(bound);
		}
		int boundsOffset = this.fb.endVector();

		CompoundField.startValuesVector(this.fb, values.length);
		for (double val : values) {
			this.fb.addDouble(val);
		}
		int valuesOffset = this.fb.endVector();

		CompoundField.startCompoundField(this.fb);
		CompoundField.addCount(this.fb, count);
		CompoundField.addSum(this.fb, sum);
		CompoundField.addMin(this.fb, min);
		CompoundField.addMax(this.fb, max);
		CompoundField.addExplicitBounds(this.fb, boundsOffset);
		CompoundField.addValues(this.fb, valuesOffset);

		return CompoundField.endCompoundField(this.fb);
	}

	/**
	 * Reset flat buffer for reusing.
	 */
	public void reset() {
		this.fb.clear();
	}

	private void addTags(Map<String, String> tags, int[] offsets, int index) {
		for (Entry<String, String> kv : tags.entrySet()) {
			int keyOffset = this.fb.createString(kv.getKey());
			int valueOffset = this.fb.createString(kv.getValue());

			offsets[index] = KeyValue.createKeyValue(this.fb, keyOffset, valueOffset);
			index++;
		}
	}
}
