package pageObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.restassured.response.Response;
import util.function;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * 中间处理器
 * 简单处理，只有一套变量
 */
public class TestCaseProcess {
    protected Parameters params;
    protected Map<String, Restful> restfulList = new HashMap<>();

    public TestCaseProcess(String env) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Parameters parameters = mapper.readValue(TestCaseProcess.class.getResourceAsStream(function.globalConfigYamlPath), Parameters.class);
        Map<String, Object> parameterMap = (Map<String, Object>) parameters.getParameters().get(env);
        parameters.setParameters(parameterMap);
        this.params = parameters;
    }

    public void setParameters(String key, Object value) {
        params.add(key, value);
    }

    public Parameters getParams() {
        return params;
    }

    public void addRestful(String name, Restful restful) {
        this.restfulList.put(name, restful);
    }

    public Restful getRestful(String name) {
        return restfulList.get(name);
    }

    //验证jsonpath
    public static void main(String[] args) {
    }

    public void print() {
        System.out.println(getParams().getParameters());
    }

}
