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

import io.lindb.client.Client;
import io.lindb.client.ClientFactory;
import io.lindb.client.Options;
import io.lindb.client.api.Point;
import io.lindb.client.api.Write;

public class WritePoint {
	public static void main(String[] args) throws Exception {
		Options options = Options.builder()
				.addDefaultTag("region", "shanghai")
				.useGZip(true).batchSize(5).flushInterval(1000)
				.build();
		// create LinDB Client with broker endpoint
		Client client = ClientFactory.create("http://localhost:9000", options);
		// get write for database
		Write write = client.write("_internal");

		for (int i = 0; i < 10; i++) {
			Point point = Point.builder("host.cpu").addLast("load", 1.0)
					.addTag("ip", "1.1.1." + i).build();
			boolean ok = write.put(point);
			System.out.println("write status: " + ok);
		}
		// need close write after write done
		write.close();
		System.out.println("done");
	}
}
