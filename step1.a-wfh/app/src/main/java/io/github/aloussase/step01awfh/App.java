package io.github.aloussase.step01awfh;

import io.github.aloussase.step01awfh.config.Configuration;
import io.github.aloussase.step01awfh.services.Base64Encoder;
import io.github.aloussase.step01awfh.services.MD5WfhService;
import io.github.aloussase.step01awfh.services.PlainTextEncoder;
import io.github.aloussase.step01awfh.services.WfhService;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;


public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        Configuration config = Configuration.the();

        WfhService hasher = new MD5WfhService();
        // PlainTextEncoder encoder = new Base64Encoder();

        StreamsBuilder builder = new StreamsBuilder();

        var stream = builder.stream(config.getInputTopic(),
                Consumed.with(
                        Serdes.Void(),
                        Serdes.String()));

        stream
                .peek((_key, value) -> logger.info("Received value: " + value))
                .mapValues(hasher::hashToString)
                .peek((_key, value) -> logger.info("Producing value: " + value))
                .to(config.getOutputTopic(), Produced.with(
                        Serdes.Void(),
                        Serdes.String()));

        final var topology = builder.build();
        final var app = new KafkaStreams(topology, config.getProps());
        final var latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down");
            app.close();
            latch.countDown();
        }));

        logger.info("Topology: " + topology.describe());

        try {
            app.start();
            latch.await();
        } catch (Throwable ex) {
            logger.info("Exception thrown: " + ex.getMessage());
            System.exit(1);
        }

        logger.info("Exiting application normally");
        System.exit(0);
    }
}
