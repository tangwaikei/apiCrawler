package pageObject;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import util.function;

import java.io.IOException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Request {
    public String url;
    public String method;
    public Map<String, Object> headers;
    public Map<String, Object> json;
    public Map<String, Object> query;

    public Restful toRequest() throws IOException {
        Restful restful = new Restful();
        //APP 限制 超过5秒都没响应数据就会处理成无数据
        HttpClientConfig httpClientConfig = HttpClientConfig.httpClientConfig().
                setParam("http.connection.timeout",5000).
                setParam("http.socket.timeout",10000);

        EncoderConfig encoderConfig = new EncoderConfig("UTF-8", "UTF-8");

        RestAssured.config= RestAssuredConfig.config().
                httpClient(httpClientConfig)
                .encoderConfig(encoderConfig);

        RestAssured.baseURI = function.getGlobalParamsByKey("baseUrl").toString();

        RequestSpecification request = given();

        if(headers != null) {
            request.headers(headers);
            restful.setHeader(headers);
        }

        if(json != null) {
            request.body(json);
            restful.setBody(json);
        } else if(query != null) {
            request.queryParams(query);
            restful.setQuery(query);
        }

        restful.setMethod(method);
        restful.setUrl(url);

        Response response = request
                .when()
                .log()
                .all()
                .request(method, url)
                .then()
//                .log()
//                .all()
                .extract()
                .response();
        restful.setResponse(response);
        restful.setContext(response);

        return restful;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return this.method;
    }

    public void setHeaders(Map<String, Object> headers){
        this.headers = headers;
    }

    public Map<String, Object> getHeaders() {
        return this.headers;
    }

    public void setJson(Map<String, Object> json){
        this.json = json;
    }

    public void setQuery(Map<String, Object> query){
        this.query = query;
    }
}
