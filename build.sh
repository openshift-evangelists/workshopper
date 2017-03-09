#!/bin/bash

mvn clean package

docker build -t mjelen/workshopper .
