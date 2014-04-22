#!/bin/sh

export CLASSPATH=./classes:./lib/twitter4j-async-3.0.5.jar:./lib/twitter4j-media-support-3.0.5.jar:./lib/twitter4j-core-3.0.5.jar:./lib/twitter4j-stream-3.0.5.jar:./lib/mysql-connector-java-5.1.14-bin.jar

java -cp $CLASSPATH com.conniffe.brezina.harvey.main 


