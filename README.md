jixture
=======

[![Build Status](https://travis-ci.org/cpollet/jixture.svg?branch=master)](https://travis-ci.org/cpollet/jixture)

jixture is an open source (Apache 2 licensed) spring/hibernate based java fixtures loading framework.

Several custom maven flags are available:

* `-DskipOracleTests=true` skip tests under `/integration/oracle`
* `-DskipMysqlTests=true`  skips tests uner `/integration/mysql`
* `-DskipSampleTests=true` skips tests under `/sample`
* `-Djixture.sample.database.type=[oracle,mysql]`, defaults to `oracle` determine what database should be used to run tests under `/samples`

Since the oracle driver is not in Central, it is not included in the dependencies if a file named `.ojdbc6.exclude` exists in `/`.
