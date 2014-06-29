# Prepare environment for MySQL integration tests

## Configure MySQL
Make sure tx_isolation=READ-COMMITTED:

$ mysql -uroot -e 'SET GLOBAL TRANSACTION ISOLATION LEVEL READ COMMITTED'