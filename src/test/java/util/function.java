package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pageObject.ApiTest;
import pageObject.Parameters;
import java.io.IOException;
import java.util.Map;

public class function {
    public static final String globalConfigYamlPath = "config\\global.yaml";

    public static Object getGlobalParamsByKey(String key) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Parameters parameters = mapper.readValue(ApiTest.class.getResourceAsStream(globalConfigYamlPath), Parameters.class);
        Map<String, Object> params = parameters.getParameters();
        return ((Map<String, Object>) params.get("pro")).get(key);
    }
}
