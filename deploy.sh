#!/bin/bash
mvn clean
mvn install
cp target/*.war ~/Desktop/F18/02265/camunda-bpm-*/server/wildfly*/standalone/deployments/

