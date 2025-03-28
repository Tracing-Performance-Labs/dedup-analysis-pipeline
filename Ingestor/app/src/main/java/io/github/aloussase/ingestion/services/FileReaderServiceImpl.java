package io.github.aloussase.ingestion.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileReaderServiceImpl implements FileReaderService {
    private final Path filename;
    private final boolean isDirectory;
    private static final Logger logger = Logger.getLogger(FileReaderServiceImpl.class.getName());

    public FileReaderServiceImpl(String filename) {
        this.filename = Paths.get(filename);
        this.isDirectory = Files.isDirectory(this.filename);
    }

    @Override
    public void onEachLine(Consumer<String> cb) {
        if (isDirectory) return;

        try (var lines = Files.lines(this.filename)) {
            lines.forEach(cb);
        } catch (IOException ignored) {
            logger.log(Level.WARNING, "Failed to read lines from: " + this.filename);
        }
    }

    @Override
    public Iterable<String> lines() {
        if (isDirectory) return List.of();

        try (var lines = Files.lines(this.filename)) {
            return lines.toList();
        } catch (IOException ignored) {
            logger.log(Level.WARNING, "Failed to read lines from: " + this.filename);
            return List.of();
        }
    }
}
