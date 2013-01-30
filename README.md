== sstableloader-csv

This tool parses CSV files into sstables for Apache Cassandra. It was written for a project involving the need to parse ~75 Million row CSV files.

=== Quickstart

Run the 'run' script with 3 arguments - <keyspace> <column> <input>

For example:
       ./run.sh Test Data test.csv

=== Requirements

*$CASSANDRA_HOME must be set to the location of the apache-cassandra.jar & associated libraries.
*$CASSANDRA_CONFIG must be set to the location of your cassandra.yml config.

=== Note

The number of columns & names are coded in DataImport.java, you'll need to change these yourself. There isn't also any error handling in the parsing, sorry.
