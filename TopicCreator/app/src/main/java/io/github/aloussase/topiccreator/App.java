package io.github.aloussase.topiccreator;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        final var brokerAddr = Objects.requireNonNull(System.getenv("KAFKA_ADDRESS"), "Broker address is not set");

        final var props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddr);

        final var topicNames = List.of(
                "step0-ingestion",
                "step01-a-wfh",
                "fastcdc-processor",
                "ast-processor"
        );

        final var topics = topicNames
                .stream()
                .map(topicName -> new NewTopic(topicName, 1, (short) 1))
                .toList();

        try (final var client = AdminClient.create(props)) {
            client.createTopics(topics);
        } catch (Exception ex) {
            System.err.println("Failed to create topics: " + ex.getMessage());
        }

        System.out.println("Created topics");
        System.exit(0);
    }
}
