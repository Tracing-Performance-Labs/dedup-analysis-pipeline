#!/bin/bash

export KAFKA_ADDRESS="localhost:9092"

# Ingestion step
export INGESTION_TOPIC="step0-ingestion"
export INGESTION_DATA_DIR="./data"

# Preprocessing steps

# Whole File Hashing
export STEP_01_A_WFH_TOPIC_INPUT_NAME="$INGESTION_TOPIC"
export STEP_01_A_WFH_TOPIC_OUTPUT_NAME="step01-a-wfh"
