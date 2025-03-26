## Create topics

```bash
./kafka-topics.sh --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic <topic-name>
```

And do that for each topic:

- step0-ingestion
- step01-a-wfh

## Consume from a topic

```bash
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic <topic-name> --from-beginning
```
