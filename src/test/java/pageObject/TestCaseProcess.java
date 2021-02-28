package pageObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.restassured.response.Response;

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
    final String globalYamlPath = "D:\\Java\\code\\apiCrawler\\src\\test\\resources\\config\\global.yaml";

    public TestCaseProcess(String env) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Parameters parameters = mapper.readValue(new File(globalYamlPath), Parameters.class);
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
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", "1336929967480642270");
        map.put("goodsOrderPreList", Collections.emptyList());
        map.put("phpUserId", "5319124");
        map.put("sign", "string");
        Map<String, String> headers = new HashMap<>();
        headers.put("token", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjgwODIwNzc2NDEyNTE2MzYwIiwiYm9keSI6IntcImlkZW50aWZpZXJcIjpcIjU2Mjg1Njk2N1wiLFwibmlja25hbWVcIjpcIlwiLFwic2V4XCI6MCxcInVzZXJJZFwiOjEyODA4MjA3NzY0MTI1MTYzNjB9IiwicmFuIjotMTk2MTg3OTAxNn0.BuIj6kskVEB4V1oRAAYlvM_naitjd-_lZu70hxWbqbw27FXTATKHp69q_G7N6IlqPHMhyfO2kgw2ANU3ynUuZw");
        headers.put("phpUserId", "5319124");
        Response response = given()
                .headers(headers)
                .body(map)
                .log().all()
                .when()
                .get("https://pyp-api.chuxingyouhui.com/api/knightCard/goods/v1_0/fends")
                .then().log().all().extract().response();
        String body = response.body().asString();
        ReadContext ctx =  JsonPath.parse(body);
//        String fenceId = ctx.read("$.data[?( @.fenceName == '骑士优选' )].fenceId");
//        System.out.println(fenceId);
    }

    public void print() {
        System.out.println(getParams().getParameters());
    }

}
