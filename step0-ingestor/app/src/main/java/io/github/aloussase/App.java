package io.github.aloussase;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.github.aloussase.ingestion.services.FileReaderServiceFactory;
import io.github.aloussase.ingestion.services.KafkaRecordProducerService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.logging.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public void handle(HttpExchange ex) throws IOException {
        ex.sendResponseHeaders(200, 0);
        ex.getResponseBody().close();

        var dataDir = Objects.requireNonNull(System.getenv("INGESTION_DATA_DIR"), "Ingestion data dir is not set");
        try (
                var files = Files.walk(Paths.get(dataDir));
                var producer = new KafkaRecordProducerService()
        ) {
            files.forEach(file -> {
                logger.info("Processing file: " + file.getFileName());

                final var filename = file.toString();
                final var reader = FileReaderServiceFactory.create(filename);

                for (final var line : reader.lines()) {
                    logger.info("Sending line: " + line.substring(0, 20));
                    producer.send(line);
                }
            });
        } catch (IOException ioEx) {
            System.err.printf("There was an error while opening directory '%s': %s%n", dataDir, ioEx.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        final var server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8000), 0);
        final var app = new App();
        server.createContext("/", app::handle);
        server.setExecutor(null);
        server.start();
    }
}
