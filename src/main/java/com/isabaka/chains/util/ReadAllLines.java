package com.isabaka.chains.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class ReadAllLines implements Function<File, Stream<String>> {

    @Override
    public Stream<String> apply(File file) {
        return readAllLines(file.toPath()).stream();
    }

    private static List<String> readAllLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
