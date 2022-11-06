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

/**
 * Data point compound field for storing histogram data.
 */
public class CompoundField implements Field {
	private final double min;
	private final double max;
	private final double count;
	private final double sum;
	private final double[] bounds;
	private final double[] values;

	/**
	 * Create a compound field instance.
	 * Notice: bounds length must be equals values length
	 * 
	 * @param count  count value
	 * @param sum    sum value
	 * @param min    min value
	 * @param max    max value
	 * @param bounds bound array
	 * @param values value array
	 */
	protected CompoundField(double count, double sum, double min, double max, double[] bounds, double[] values) {
		this.count = count;
		this.sum = sum;
		this.min = min;
		this.max = max;
		this.bounds = bounds;
		this.values = values;
	}

	/*
	 * Write compound field into {@link RowBuilder}.
	 * 
	 * @see io.lindb.client.api.Field#write(io.lindb.client.api.RowBuilder)
	 */
	@Override
	public int write(RowBuilder builder) {
		return builder.addCompoundField(this.count, this.sum, this.min, this.max, this.bounds, this.values);
	}

	/**
	 * Return count value, just for testing.
	 * 
	 * @return count value
	 */
	double getCount() {
		return this.count;
	}
}
