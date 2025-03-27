package io.github.tracingperformancelabs.dupe;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.concurrent.CountDownLatch;

public class App {
    public static void main(String[] args) {
        final var app = createApp();
        final var latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.close();
            latch.countDown();
        }));

        try {
            app.start();
            latch.await();
        } catch (Throwable ex) {
            System.err.println(ex);
            System.exit(1);
        }

        System.exit(0);
    }


    static KafkaStreams createApp() {
        final var builder = new StreamsBuilder();
        final var configuration = Configuration.the();

        // TODO: Configure stream processor

        final var topology = builder.build();
        final var app = new KafkaStreams(topology, configuration.getProperties());
        return app;
    }
}
