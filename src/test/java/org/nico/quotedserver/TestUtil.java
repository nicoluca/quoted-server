package org.nico.quotedserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.fail;

public class TestUtil {

    public static String resourceToString(String resourcePath) {
        // Base path is src/test/resources
        try {
            Path path = Path.of(Objects.requireNonNull(TestUtil.class.getResource(resourcePath)).getPath());
            return Files.readString(path);
        } catch (IOException e) {
            fail("Could not read resource: " + resourcePath);
            throw new RuntimeException(e);
        }
    }
}
