package io.github.tracingperformancelabs.dupe;

import io.github.tracingperformancelabs.dupe.services.AstProcessorService;
import io.github.tracingperformancelabs.dupe.services.JsonAstProcessorService;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class App {
    public static void main(String[] args) {
        final var processor = new JsonAstProcessorService();
        final var config = Config.the();

        final var streamsBuilder = new StreamsBuilder();
        final var app = createPipeline(streamsBuilder, config, processor);

        final var latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            app.close();
            latch.countDown();
        }));

        try {
            app.start();
            latch.await();
        } catch (Exception ex) {
            System.exit(1);
        }

        System.exit(0);
    }

    private static KafkaStreams createPipeline(
            StreamsBuilder builder,
            Config config,
            AstProcessorService<Object, String> processor
    ) {
        final var stream = builder.stream(config.getInputTopic());

        stream
                .peek((_key, value) -> System.out.println("Incoming message: " + value))
                .flatMapValues(span -> {
                    final var xs = new ArrayList<String>();
                    processor.visit(span, xs::add);
                    return xs;
                })
                .peek((_key, value) -> System.out.println("Sending to output: " + value))
                .to(config.getOutputTopic());

        final var topology = builder.build();
        final var app = new KafkaStreams(topology, config.getProps());

        return app;
    }
}
