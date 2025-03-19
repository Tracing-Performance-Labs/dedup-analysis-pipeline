package io.github.aloussase.step01awfh.config;

import java.util.Properties;

public class Configuration {

    final static String INPUT_TOPIC_NAME_VARIABLE = "STEP_01_A_WFH_TOPIC_INPUT_NAME";
    final static String OUTPUT_TOPIC_NAME_VARIABLE = "STEP_01_A_WFH_TOPIC_OUTPUT_NAME";

    private String inputTopic;
    private String outputTopic;
    private final Properties props = new Properties();

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

        props.setProperty("application.id", "dedupe-pipeline");
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("auto.offset.reset", "earliest");
    }


    public static Configuration the() {
        if (INSTANCE == null) {
            INSTANCE = new Configuration();
            INSTANCE.setup();
        }
        return INSTANCE;
    }

    public String getInputTopic() {
        return inputTopic;
    }

    public String getOutputTopic() {
        return outputTopic;
    }

    public Properties getProps() {
        return props;
    }
}
