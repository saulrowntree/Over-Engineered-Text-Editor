#! /bin/bash
echo Starting Build Process...
mvn package -DskipTests
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -jar target/editor-reverseproxy.jar
