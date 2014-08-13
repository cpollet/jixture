# jixture

[![Build Status](https://travis-ci.org/cpollet/jixture.svg?branch=master)](https://travis-ci.org/cpollet/jixture)

jixture is an open source (Apache 2 licensed) spring/hibernate based java fixtures loading framework.

It can load data from varous sources, such as:

 * [DbUnit](http://dbunit.sourceforge.net/)-like XML files (but without the DTD);
 * Plain SQL files;
 * Entities instances build from code, Spring context or generated automatically.

Is is possible to write you own loader as well to load, for instance, CSV or Excel files.

**External resources**

 * jixture home: [http://jixture.cpollet.net](http://jixture.cpollet.net)
 * [javadoc](http://jixture.cpollet.net/jixture-core/apidocs/index.html)

[Maven](maven.md) commands.

# Sample

A full sample using `jixture-hibernate3` is available under `sample/hibernate3`.

## Loading XML file
First, we need to det an instance of a `DatabaseTestSupport`. The following Soring context will do the trick. It only supposes that you have 

```XML
<!-- import jixture context -->
<alias name="transactionManager" alias="jixture.core.transactionManager"/>
<import resource="classpath:/spring/jixture-core-context.xml"/>

<!-- create test support bean -->
<bean id="commitDatabaseTestSupport" class="net.cpollet.jixture.support.CommitDatabaseTestSupport" />
```

This creates an instance of `CommitDatabaseTestSupport` that you can use to load data:

```Java
commitDatabaseTestSupport
	.addFixtures(new XmlFileFixture("/path/to/file.xml")) //
	.loadFixtures();
```

# Dependencies
You must provide at least the following dependencies to use `jixture-core`:

```XML
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
```

If you plan using `jixture-hibernate3` (which brings `jixture-core`), you have to provide the following dependencies as well:

```XML
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

```XML
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

```Java
DateTime start;
DateTime stop;

SomeModel template = new SomeModel().setAttribute(value);

Fixture fixture = GeneratedFixture.from(template)
	.addFieldGenerator("insertionDate", FieldGenerators.sequence(start, stop));
```

Requires Maven dependency:

```XML
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

```XML
<dependency>
	<groupId>org.hamcrest</groupId>
	<artifactId>hamcrest-core</artifactId>
	<version>1.3</version>
	<optional>true</optional>
</dependency>
```

