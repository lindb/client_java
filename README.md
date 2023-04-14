# LinDB Client Java

[![LICENSE](https://img.shields.io/github/license/lindb/client_java)](https://github.com/lindb/client_java/blob/main/LICENSE)
[![codecov](https://codecov.io/gh/lindb/client_java/branch/main/graph/badge.svg)](https://codecov.io/gh/lindb/client_java)
[![Github Actions Status](https://github.com/lindb/client_java/workflows/LinDB%20Client%20Java%20CI/badge.svg)](https://github.com/lindb/client_java/actions?query=workflow%3A%22LinDB+Client+Java+CI%22)
[![Maven Central](https://img.shields.io/maven-central/v/io.lindb/lindb-client)](https://repo1.maven.org/maven2/io/lindb/)

This repository contains the reference Java client for LinDB.

- [Features](#features)
- [How To Use](#how-to-use)
  - [Installation](#installation)
  - [Write Data](#write-data)
  - [Data Query](#data-query)
  - [State Query](#state-query)
  - [Metadata Manager](#metadata-manager)
  - [Options](#options)

## Features

- Write data
  - Write data use asynchronous/synchronous
  - Support field type(sum/min/max/last/first/histogram)
  - [FlatBuf Protocol](https://github.com/lindb/common/blob/main/proto/v1/metrics.fbs)
- Query
  - Metric data/metadata
  - Storage state
  - Broker state
  - Database state
  - Replication state

## How To Use

This clients are hosted in Maven central Repository.

If you want to use it with the Maven, you have to add only the dependency on the artifact.

### Installation

Download the latest version:

##### Maven dependency:

```XML
<dependency>
    <groupId>io.lindb</groupId>
    <artifactId>lindb-client</artifactId>
    <version>0.1.0</version>
</dependency>
```
       
##### Or when using Gradle:

```groovy
dependencies {
    implementation "io.lindb:lindb-client:0.1.0"
}
```

### Write data

Example: 
- [Asynchronous write](./src/test/java/io/lindb/client/example/WritePoint.java)
- [Synchronous write](./src/test/java/io/lindb/client/example/BlockingWritePoint.java)

```java
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
		client.close();
	}
}
```

### Data query

Example: [MetricDataQuery.java](./src/test/java/io/lindb/client/example/MetricDataQuery.java)

```java
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
```

### State query

Example: [StateQuery.java](./src/test/java/io/lindb/client/example/StateQuery.java)

```java
package io.lindb.client.example;

import java.util.ArrayList;
import java.util.List;

import io.lindb.client.Client;
import io.lindb.client.ClientFactory;
import io.lindb.client.Options;
import io.lindb.client.model.Master;

public class StateQuery {
	public static void main(String[] args) throws Exception {
		Options options = Options.builder().build();
		// create LinDB Client with broker endpoint
		Client client = ClientFactory.create("http://localhost:9000", options);
		// create state query api
		io.lindb.client.api.StateQuery query = client.stateQuery();
		System.out.println("master: =>" + query.master());
		System.out.println("databases: =>" + query.databases());
		System.out.println("database names: =>" + query.databaseNames());
		System.out.println("broker alive nodes: =>" + query.brokerAliveNodes());
		System.out.println("alive storages: =>" + query.aliveStorages());
		System.out.println("storages: =>" + query.storages());
		List<String> metrics = new ArrayList<>();
		metrics.add("lindb.monitor.system.cpu_stat");
		metrics.add("lindb.monitor.system.mem_stat");
		System.out.println("broker runtime metrics: =>" + query.brokerMetrics(metrics));
		System.out.println("storage runtime metrics: =>" + query.stroageMetrics("/lindb-cluster", metrics));
		System.out.println("replication state: =>" + query.dataFamilyState("/lindb-cluster", "_internal"));
		System.out.println("data family state: =>" + query.replicationState("/lindb-cluster", "_internal"));
		System.out.println("common ql: =>" + query.query("show master", Master.class));
		client.close();
	}
}
```

### Metadata manager

Example: [MetadataManager.java](./src/test/java/io/lindb/client/example/MetadataManager.java)

### Options

1. [Options](./src/main/java/io/lindb/client/Options.java)
2. [HttpOptions](./src/main/java/io/lindb/client/internal/HttpOptions.java)
3. [WriteOptions](./src/main/java/io/lindb/client/api/WriteOptions.java)
