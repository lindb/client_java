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
  - [Options](#options)

## Features

- Write data
  - Write data use asynchronous
  - Support field type(sum/min/max/last/first/histogram)
  - [FlatBuf Protocol](https://github.com/lindb/common/blob/main/proto/v1/metrics.fbs)

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
    <version>0.0.1</version>
</dependency>
```
       
##### Or when using Gradle:

```groovy
dependencies {
    implementation "io.lindb:lindb-client:0.0.1"
}
```

### Write data

Example: [WritePoint.java](https://github.com/lindb/client_java/blob/main/src/test/java/io/lindb/client/example/WritePoint.java)

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
	}
}
```

### Options

1. [Options](https://github.com/lindb/client_java/blob/main/src/main/java/io/lindb/client/Options.java)
2. [HttpOptions](https://github.com/lindb/client_java/blob/main/src/main/java/io/lindb/client/internal/HttpOptions.java)
3. [WriteOptions](https://github.com/lindb/client_java/blob/main/src/main/java/io/lindb/client/api/WriteOptions.java)
