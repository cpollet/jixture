# Prepare environment for Oracle integration tests

## Setup Oracle 11 XE
The method described here uses Vagrant.

1. Install Vagrant (http://www.vagrantup.com/downloads.html)
2. Install VirtualBox (https://www.virtualbox.org/wiki/Downloads)
3. Follow instructions on https://github.com/hilverd/vagrant-ubuntu-oracle-xe

## Prepare Oracle driver
Since Oracle's driver cannot be deployed in Central, you have to either use your own maven repository or manually import
Oracle's drivers in your local maven repository.

1. Download ojdbc6.jar from oracle (http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-112010-090769.html)
2. Install it mvn install:install-file -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.4 -Dpackaging=jar -Dfile=ojdbc6.jar -DgeneratePom=true

You are ready to run the test class.