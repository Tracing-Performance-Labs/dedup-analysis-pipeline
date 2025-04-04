package io.github.tracingperformancelabs.dupe;

import org.apache.kafka.streams.StreamsConfig;

import java.util.Objects;
import java.util.Properties;

public class Configuration {
    private static Configuration INSTANCE;

    public Properties getProperties() {
        return properties;
    }

    private final Properties properties;
    private final String wholeFileHashingTopic = "step01-a-wfh";
    private final String fastcdcTopic = "fastcdc-processor";
    private final String astTopic = "ast-processor";

    private Configuration() {
        this.properties = new Properties();
        properties.setProperty(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                Objects.requireNonNull(System.getenv("KAFKA_ADDRESS"), "Broker location not set"));
        properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "dedupe-pipeline-db-sink-" + System.currentTimeMillis());
    }

    public static Configuration the() {
        if (INSTANCE == null) {
            INSTANCE = new Configuration();
        }
        return INSTANCE;
    }

    public String getWholeFileHashingTopic() {
        return wholeFileHashingTopic;
    }

    public String getFastcdcTopic() {
        return fastcdcTopic;
    }

    public String getAstTopic() {
        return astTopic;
    }
}
