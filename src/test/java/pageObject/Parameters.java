package pageObject;

import java.util.HashMap;
import java.util.Map;

public class Parameters {
    public Map<String, Object> parameters = new HashMap<>();

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    public void add(String key, Object value) {
        parameters.put(key, value);
    }

    public Object find(String key) {
        if(parameters.containsKey(key)) {
            return parameters.get(key);
        }
        //TODO
        return null;
    }
}

