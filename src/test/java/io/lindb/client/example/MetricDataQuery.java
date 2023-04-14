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
package io.lindb.client.example;

import java.util.List;

import io.lindb.client.Client;
import io.lindb.client.ClientFactory;
import io.lindb.client.Options;
import io.lindb.client.api.DataQuery;
import io.lindb.client.model.Field;
import io.lindb.client.model.Metadata;
import io.lindb.client.model.ResultSet;

public class MetricDataQuery {

	public static void main(String[] args) throws Exception {
		Options options = Options.builder().build();
		// create LinDB Client with broker endpoint
		Client client = ClientFactory.create("http://localhost:9000", options);
		// create metric data query
		DataQuery query = client.dataQuery();
		ResultSet rs = query.dataQuery("_internal",
				"select heap_objects from lindb.runtime.mem where 'role' in ('Broker') group by node");
		System.out.println(rs);
		Metadata<List<String>> tags = query.metadataQuery("_internal", "show tag keys from lindb.runtime.mem");
		System.out.println(tags);
		Metadata<List<Field>> fields = query.metadataQuery("_internal", "show fields from lindb.runtime.mem");
		System.out.println(fields);
		client.close();
	}
}
