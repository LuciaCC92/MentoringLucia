package Utilities;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LoadResource {
    public static String loadResource(String path) {
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(path);
        if (is == null) {
            throw new RuntimeException("No se encontró el recurso: " + path);
        }
        try {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo el recurso: " + path, e);
        }
    }
}
