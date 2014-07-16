# jixture

[![Build Status](https://travis-ci.org/cpollet/jixture.svg?branch=master)](https://travis-ci.org/cpollet/jixture)

jixture is an open source (Apache 2 licensed) spring/hibernate based java fixtures loading framework.

jixture home: http://jixture.cpollet.net

Several custom maven flags are available:

* `-DskipOracleTests=true` skip tests under `/integration/oracle`
* `-DskipMysqlTests=true`  skips tests uner `/integration/mysql`
* `-DskipSampleTests=true` skips tests under `/sample`
* `-Djixture.sample.database.type=[oracle,mysql]`, defaults to `oracle` determine what database should be used to run tests under `/samples`

Since the oracle driver is not in Central, it is not included in the dependencies if a file named `.ojdbc6.exclude` exists in `/`.

# Dependencies
Some dependencies are optional. This section aims at clarifying the use cases in which they are required.

## [Java Deep-Cloning library](https://code.google.com/p/cloning/) 

This library is required when you want to use the ```TemplateGenerator``` entitiy generator as a ```FixtureGenerator``` to be used in a ```GeneratedFixture```.

For instance:

```Java
SomeModel template = new SomeModel().setAttribute(value);
Fixture fixture = GeneratedFixture.from(template);
```

```
<dependency>
	<groupId>uk.com.robust-it</groupId>
	<artifactId>cloning</artifactId>
	<version>1.9.0</version>
</dependency>
```

