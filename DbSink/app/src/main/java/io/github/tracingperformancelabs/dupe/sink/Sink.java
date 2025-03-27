package io.github.tracingperformancelabs.dupe.sink;

import io.github.tracingperformancelabs.dupe.Configuration;
import io.github.tracingperformancelabs.dupe.model.DataPoint;
import io.github.tracingperformancelabs.dupe.services.DataStorageService;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;

import java.util.UUID;

public class Sink {
    public static class OutputSink {
        private final StreamsBuilder builder;
        private DataStorageService<String> dataStore;

        private OutputSink(StreamsBuilder builder) {
            this.builder = builder;
        }

        // TODO: Think of a better name for this.

        public void drain(String topic) {
            final var stream = builder.stream(topic, Consumed.with(Serdes.Void(), Serdes.String()));
            stream.foreach((_key, value) -> {
                final var id = UUID.randomUUID().toString();
                dataStore.store(new DataPoint<>(id, value, topic));
            });
        }
    }

    public static OutputSink from(StreamsBuilder builder) {
        return new OutputSink(builder);
    }

}
