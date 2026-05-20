package Utilities;

import java.util.Map;

public class JsonPayloaderBuilder {
    public static String build(String templatePath, Map<String, ?> values) {
        // Leemos desde cache (rápido)
        //String content = TemplateCache.get(templatePath);
        String content = null;
        if (values != null) {
            for (Map.Entry<String, ?> entry : values.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();

                String replacement = (val == null) ? "" : val.toString();
                content = content.replace("{{" + key + "}}", replacement);
            }
        }
        return content;
    }
}
