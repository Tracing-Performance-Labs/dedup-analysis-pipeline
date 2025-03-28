package io.github.aloussase.ingestion.services;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.VoidSerializer;

import java.util.Objects;
import java.util.Properties;

public class KafkaRecordProducerService implements RecordProducerService<String>, AutoCloseable {
    private final KafkaProducer<Void, String> producer;
    private final String ingestionTopic;
    private final String brokerAddr;

    public KafkaRecordProducerService() {
        ingestionTopic = Objects.requireNonNull(System.getenv("INGESTION_TOPIC"), "Ingestion topic is not set");
        brokerAddr = Objects.requireNonNull(System.getenv("KAFKA_ADDRESS"), "Broker address is not set");

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddr);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, VoidSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void send(String record) {
        producer.send(new ProducerRecord<>(ingestionTopic, record));
    }

    @Override
    public void close() {
        this.producer.close();
    }
}
