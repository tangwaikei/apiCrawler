package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import base.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class function {
    public static Object getGlobalParamsByKey(String key) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Parameters parameters = mapper.readValue(new File("D:\\Java\\code\\shopapitest\\src\\test\\resources\\config\\global.yaml"), Parameters.class);
        Map<String, Object> params = parameters.getParameters();
        return ((Map<String, Object>) params.get("pro")).get(key);
    }
}
