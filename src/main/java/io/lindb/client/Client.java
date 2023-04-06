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

import java.io.IOException;

import io.lindb.client.api.DataQuery;
import io.lindb.client.api.EventListener;
import io.lindb.client.api.MetadataManager;
import io.lindb.client.api.StateQuery;
import io.lindb.client.api.Write;

/**
 * LinDB client interface.
 */
public interface Client {
	/**
	 * Create write client.
	 * 
	 * @param database database name {@link String}
	 * @return write client {@link Write}
	 * @throws IOException if send data error
	 */
	Write write(String database) throws IOException;

	/**
	 * Create write client.
	 * 
	 * @param database database name {@link String}
	 * @param listener the listener to listen events
	 * @return write client {@link Write}
	 * @throws IOException if send data error
	 */
	Write write(String database, EventListener listener) throws IOException;

	/**
	 * Create metric data query client.
	 * 
	 * @return metric data client {@link DataQuery}
	 */
	DataQuery dataQuery();

	/**
	 * Create system state query client.
	 * 
	 * @return state query client {@link StateQuery}
	 */
	StateQuery stateQuery();

	/**
	 * Create metadata manager client.
	 * 
	 * @return metadata manager client {@link MetadataManager}
	 */
	MetadataManager metadataManager();
}
