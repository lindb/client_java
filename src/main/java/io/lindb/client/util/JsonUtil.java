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
package io.lindb.client.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonUtils is a singleton ObjectMapper.
 */
public class JsonUtil {
	private JsonUtil() {
	}

	/**
	 * Singleton ObjectMapper for all transport related libraries
	 */
	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}

	/**
	 * Object to json string.
	 *
	 * @param object JsonNode or POJO
	 * @return JSON String
	 * @throws Exception throws exception when fail
	 */
	public static String toString(Object object) throws Exception {
		return objectMapper.writeValueAsString(object);
	}

	/**
	 * Json string to object
	 * 
	 * @param json  json string
	 * @param <T>   Object type
	 * @param clazz result class
	 * @return T object
	 * @throws Exception throws exception when fail
	 */
	public static <T> T toObject(String json, Class<T> clazz) throws Exception {
		return objectMapper.readValue(json, clazz);
	}
}
