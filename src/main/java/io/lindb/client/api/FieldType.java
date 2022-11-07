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
 * Simple field type.
 */
public enum FieldType {
	Last(1),
	Sum(2),
	Min(3),
	Max(4),
	First(5);

	private byte val;

	private FieldType(int val) {
		this.val = (byte) val;
	}

	/**
	 * Return the value of field type.
	 * 
	 * @return the value of field type
	 */
	public byte value() {
		return this.val;
	}
}
