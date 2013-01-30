#!/bin/sh

JAVA=`which java`
CLASSPATH=".:$CASSANDRA_CONFIG:$CASSANDRA_JAR"

for jar in $CASSANDRA_HOME/*.jar; do
    CLASSPATH=$CLASSPATH:$jar
done

# Compile
javac -cp $CLASSPATH DataImport.java

# Import
$JAVA -ea -cp $CLASSPATH -Xmx512M -Dlog4j.configuration=log4j-tools.properties DataImport "$@"
