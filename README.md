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
You must provide at least the following dependencies to use jixture-core:

```
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context</artifactId>
	<version>3.0.0.RELEASE</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-orm</artifactId>
	<version>3.0.0.RELEASE</version>
</dependency>
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-core</artifactId>
	<version>3.5.0-Final</version>
</dependency>
<dependency>
	<groupId>org.hibernate.javax.persistence</groupId>
	<artifactId>hibernate-jpa-2.0-api</artifactId>
	<version>1.0.0.Final</version>
</dependency>
```


Some other dependencies are optional. Keep reading to know when they are needed.

## ```TemplateGenerator```
If you want to the the ```TemplateGenerator``` as an entity generator for an GeneratedFixture, you have to include following libs:

 * [Commons BeanUtils](http://commons.apache.org/proper/commons-beanutils/)
 * [Java Deep-Cloning library](https://code.google.com/p/cloning/)

For instance:

```Java
SomeModel template = new SomeModel().setAttribute(value);
TemplateGenerator fixture = GeneratedFixture.from(template);
```

Requies Maven dependencies:

```
<dependency>
	<groupId>commons-beanutils</groupId>
	<artifactId>commons-beanutils</artifactId>
	<version>1.9.2</version>
</dependency>
<dependency>
	<groupId>uk.com.robust-it</groupId>
	<artifactId>cloning</artifactId>
	<version>1.9.0</version>
</dependency>
```

## ```DateSequence```

If you want to use a  ```DateSequence``` field generator inside an ```TemplateGenerator``` fixture generator, you have to include [Joda-Time](http://www.joda.org/joda-time/) in your dependencies.

For instance:

```
DateTime start;
DateTime stop;

SomeModel template = new SomeModel().setAttribute(value);

Fixture fixture = GeneratedFixture.from(template)
	.addFieldGenerator("insertionDate", FieldGenerators.sequence(start, stop));
```

Requires Maven dependency:

```
<dependency>
	<groupId>joda-time</groupId>
	<artifactId>joda-time</artifactId>
	<version>2.3</version>
</dependency>
```

## ```ExtractorMatcher```
If you want to be able to extract entities from fixtures implenting ```ExtractionCapableFixture``` (such as ```GeneratedFixture``` or ```MappingFixture```), you have to include [hamcrest](https://code.google.com/p/hamcrest/) in your dependencies.

hamcrest is required when you want to use the  in fixtures implenting .

For instance:

```Java
Matcher someHamcrestMarcher;

MappingFixture fixture = new MappingFixture(entity1, entity2)
	.addExtractorMatcher(ExtractionMatcher.create(someHamcrestMatcher));
```

Requires Maven dependency:

```
<dependency>
	<groupId>org.hamcrest</groupId>
	<artifactId>hamcrest-core</artifactId>
	<version>1.3</version>
	<optional>true</optional>
</dependency>
```

