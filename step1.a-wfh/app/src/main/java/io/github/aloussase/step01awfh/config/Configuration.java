package io.github.aloussase.step01awfh.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class Configuration {

    private final static Logger logger = Logger.getLogger(Configuration.class.getName());

    final static String INPUT_TOPIC_NAME_VARIABLE = "STEP_01_A_WFH_TOPIC_INPUT_NAME";
    final static String OUTPUT_TOPIC_NAME_VARIABLE = "STEP_01_A_WFH_TOPIC_OUTPUT_NAME";

    private String inputTopic;
    private String outputTopic;
    private final Properties props = new Properties();

    private boolean didSetup = false;

    private static Configuration INSTANCE = null;


    private Configuration() {
    }

    private void setup() {
        final var inputTopic = System.getenv(INPUT_TOPIC_NAME_VARIABLE);
        if (inputTopic == null) {
            System.err.println("Missing input topic environment variable");
            System.exit(1);
        }

        final var outputTopic = System.getenv(OUTPUT_TOPIC_NAME_VARIABLE);
        if (outputTopic == null) {
            System.err.println("Missing output topic environment variable");
            System.exit(1);
        }

        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;

        logger.info("Using INPUT TOPIC = " + inputTopic);
        logger.info("Using OUTPUT TOPIC = " + outputTopic);

        final var brokerAddr = Objects.requireNonNull(System.getenv("KAFKA_ADDRESS"), "Broker address is not set");
        logger.info("Using broker address: " + brokerAddr);

        props.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "dedupe-pipeline-" + System.currentTimeMillis());
        props.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddr);
        props.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Void().getClass().getName());
        props.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.setProperty(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        didSetup = true;
    }


    public static Configuration the() {
        if (INSTANCE == null) {
            INSTANCE = new Configuration();
            INSTANCE.setup();
        }
        return INSTANCE;
    }

    public String getInputTopic() {
        if (!didSetup) {
            throw new IllegalStateException("Configuration has not been setup");
        }
        return inputTopic;
    }

    public String getOutputTopic() {
        if (!didSetup) {
            throw new IllegalStateException("Configuration has not been setup");
        }
        return outputTopic;
    }

    public Properties getProps() {
        if (!didSetup) {
            throw new IllegalStateException("Configuration has not been setup");
        }
        return props;
    }
}
