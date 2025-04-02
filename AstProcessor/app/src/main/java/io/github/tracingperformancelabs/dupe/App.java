package io.github.tracingperformancelabs.dupe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.concurrent.CountDownLatch;

public class App {
    public static void main(String[] args) {
        final var gson = new GsonBuilder().create();
        final var config = Config.the();

        final var streamsBuilder = new StreamsBuilder();
        final var app = createPipeline(streamsBuilder, config);

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

    private static KafkaStreams createPipeline(StreamsBuilder builder, Config config) {
        return null;
    }
}
