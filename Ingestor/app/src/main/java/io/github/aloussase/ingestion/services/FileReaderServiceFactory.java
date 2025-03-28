package io.github.aloussase.ingestion.services;

import java.util.function.Consumer;

public final class FileReaderServiceFactory {
    public static FileReaderService create(String filename) {
        return new FileReaderServiceImpl(filename);
    }
}
