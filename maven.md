# Tests
Several custom maven flags are available:

* `-DskipOracleTests=true` skip tests under `/integration/oracle`
* `-DskipMysqlTests=true`  skips tests uner `/integration/mysql`
* `-DskipSampleTests=true` skips tests under `/sample`
* `-Djixture.sample.database.type=[oracle,mysql]`, defaults to `oracle` determine what database should be used to run tests under `/samples`

Since the oracle driver is not in Central, it is not included in the dependencies if a file named `.ojdbc6.exclude` exists in `jixture/`.

# PIT

Use goal `org.pitest:pitest-maven:mutationCoverage` to run PIT

# Release
 * Release to sonatype: `mvn clean deploy -DperformRelease=true -Psonatype-oss-release`
 * Deploy site: `mvn clean site site:deploy -Dsite`
