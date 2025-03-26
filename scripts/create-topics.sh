#!/bin/bash

./kafka-topics.sh --create --bootstrap-server localhost:9092 --topic step0-ingestion
./kafka-topics.sh --create --bootstrap-server localhost:9092 --topic step01-a-wfh
