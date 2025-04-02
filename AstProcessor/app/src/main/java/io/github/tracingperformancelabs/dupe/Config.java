package io.github.tracingperformancelabs.dupe;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Objects;
import java.util.Properties;

public class Config {
    private String inputTopic;
    private String outputTopic;
    private String brokerAddr;
    private Properties props;

    private static Config INSTANCE;

    private static final String APPLICATION_ID_PREFIX = "dedupe-pipeline-ast-processor-";

    private Config() {
        setup();
    }

    private void setup() {
        inputTopic = Objects.requireNonNull(System.getenv("INGESTION_TOPIC"), "Missing ingestion topic");
        outputTopic = Objects.requireNonNull(System.getenv("AST_OUTPUT_TOPIC"), "Missing output topic");
        brokerAddr = Objects.requireNonNull(System.getenv("KAFKA_ADDR"), "Missing broker address");

        props = new Properties();
        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID_PREFIX + System.currentTimeMillis());
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddr);
        props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Void().getClass().getName());
        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.setProperty(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    public static Config the() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    public String getInputTopic() {
        return inputTopic;
    }

    public String getOutputTopic() {
        return outputTopic;
    }

    public String getBrokerAddr() {
        return brokerAddr;
    }

    public Properties getProps() {
        return props;
    }
}
