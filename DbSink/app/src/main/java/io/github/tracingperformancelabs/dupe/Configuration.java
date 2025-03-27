package io.github.tracingperformancelabs.dupe;

import java.util.Properties;

public class Configuration {
    private static Configuration INSTANCE;

    public Properties getProperties() {
        return properties;
    }

    private Properties properties;
    private String wholeFileHashingTopic;

    private Configuration() {
        this.properties = new Properties();
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
}
