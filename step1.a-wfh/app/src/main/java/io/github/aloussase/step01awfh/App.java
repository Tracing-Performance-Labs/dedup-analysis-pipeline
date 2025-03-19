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


public class App {
    public static void main(String[] args) {
        Configuration config = Configuration.the();

        WfhService hasher = new MD5WfhService();
        PlainTextEncoder encoder = new Base64Encoder();

        StreamsBuilder builder = new StreamsBuilder();

        var stream = builder.stream(config.getInputTopic(),
                Consumed.with(
                        Serdes.String(),
                        Serdes.String()));

        stream
                .mapValues(hasher::hash)
                .filter((_key, hash) -> hash != null)
                .mapValues(encoder::encode)
                .to(config.getOutputTopic(), Produced.with(
                        Serdes.String(),
                        Serdes.String()));


        final var topology = builder.build();
        final var app = new KafkaStreams(topology, config.getProps());

        Runtime.getRuntime().addShutdownHook(new Thread(app::close));

        app.start();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException ex) {
        }
    }
}
