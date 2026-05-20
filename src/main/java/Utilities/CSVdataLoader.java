package Utilities;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSVdataLoader {
    /**
     * Lee CSV UTF-8 con cabecera y devuelve lista de rows como Map<columna, valor>.
     */
    public static @NotNull List<Map<String, String>> readAsMaps(String resourcePath, char separator) throws IOException {
        InputStream is = CSVdataLoader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) throw new FileNotFoundException("No se encuentra el CSV: " + resourcePath);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String headerLine = br.readLine();
            if (headerLine == null) throw new IllegalArgumentException("CSV vacío: " + resourcePath);

            String[] headers = headerLine.split("\\" + separator, -1);
            for (int i = 0; i < headers.length; i++) {
                if (headers[i] != null) headers[i] = headers[i].trim();
            }

            List<Map<String, String>> out = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\" + separator, -1);
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String val = i < parts.length ? parts[i] : "";
                    row.put(headers[i], val == null ? "" : val.trim());
                }
                out.add(row);
            }
            return out;
        }
    }

    /**
     * Versión simplificada: usa coma como separador por defecto.
     */
    public static List<Map<String, String>> readAsMaps(String resourcePath) throws IOException {
        return readAsMaps(resourcePath, ',');
    }
}
