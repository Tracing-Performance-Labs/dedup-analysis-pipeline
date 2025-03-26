## Run with Docker

```
docker compose up -d
```

This will spin up the following:

- Kafka
- Kafka UI
- Ingestion service: Reads data from a file and sends it to Kafka
- WFH Service: Hashes whole spans and sends them to a different topic

## License
MIT
