package com.isabaka.chains.util;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public class ResourceByName implements Function<String, File> {

    @Override
    public File apply(String name) {
        try {
            return new File(this.getClass().getResource(name).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
