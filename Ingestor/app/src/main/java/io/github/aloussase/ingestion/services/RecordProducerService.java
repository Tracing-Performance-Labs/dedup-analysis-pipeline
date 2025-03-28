package io.github.aloussase.ingestion.services;

public interface RecordProducerService<TRecord> {

    void send(TRecord record);

}
