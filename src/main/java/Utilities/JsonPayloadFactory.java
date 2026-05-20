package Utilities;

import java.util.Map;

public class JsonPayloadFactory {
    public static String build(String templatePath, Map<String, ?> values) throws Exception {
        return JsonPayloaderBuilder.build(templatePath, values);
    }
}
