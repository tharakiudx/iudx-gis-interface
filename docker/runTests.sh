#!/bin/bash

nohup mvn clean compile exec:java@gis-interface & 
sleep 20
mvn clean test
cp -r target /tmp/test/