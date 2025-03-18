package io.github.aloussase;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class App {
    private static Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        var dataDir = Objects.requireNonNull(System.getenv("INGESTION_DATA_DIR"));
        var ingestionTopic = Objects.requireNonNull(System.getenv("INGESTION_TOPIC"));

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (
                var producer = new KafkaProducer<Void, String>(props);
                var files = Files.walk(Paths.get(dataDir))
        ) {
            files.forEach(file -> {
                if (Files.isDirectory(file)) {
                    return;
                }

                logger.info("Processing file: " + file.getFileName());

                try (var lines = Files.lines(file)) {
                    lines.forEach(line -> {
                        logger.info("Sending line: " + line.substring(0, 20));
                        producer.send(new ProducerRecord<>(ingestionTopic, line));
                    });
                } catch (IOException ioEx) {
                    System.err.printf("There was an error while opening file '%s': %s%n", file, ioEx.getMessage());
                }
            });
        } catch (IOException ioEx) {
            System.err.printf("There was an error while opening directory '%s': %s%n", dataDir, ioEx.getMessage());
        }
    }
}
