package io.github.aloussase.ingestion.services;

import java.util.function.Consumer;

public interface FileReaderService {
    void onEachLine(Consumer<String> cb);

    Iterable<String> lines();
}
