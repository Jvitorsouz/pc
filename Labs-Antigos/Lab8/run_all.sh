#!/bin/bash

args=`find dataset -type f | xargs`

echo "Building Java Concurrent implementation"
bash src/java/build.sh

echo "Running Concurrent implementation"
time bash src/java/run.sh $args

echo "Building Java Concurrent implementation2"
bash src/java/build.sh

echo "Running Concurrent implementation"
time bash src/java/run.sh $args