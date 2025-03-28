## Run with Docker

First, you need to create a `db.sqlite` file in the root directory.

```bash
touch db.sqlite
```

Then, create the necessary tables:

```bash
sqlite3  db.sqlite
```

```sql
create table raw_span_data (
  id text not null,
  content text not null, 
  source text not null,
  primary key (id)
);
```

Then you can run the services with the following command:

```bash
docker compose up -d
```

> [!NOTE]
> You might have to start the kafka container first, and then the other services

In order to trigger the data ingestion, you have to hit `http://localhost:8080`:

```bash
# With curl
curl http://localhost:8080

# With httpie
http :8080
```

## License
MIT
