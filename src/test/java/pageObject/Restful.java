package pageObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求+返回信息实体类
 */
public class Restful {
    private String url;
    private String method;
    private Map<String,Object> header=new HashMap<>();
    private Map<String,Object> query=new HashMap<>();
    private Map<String,Object> body=new HashMap<>();
    private Response response;
    private ReadContext context;

    @Override
    public String toString()
    {
        String responseBody =response.asString();
        //格式化json串
        try {
            JsonParser jsonParser=new JsonParser();
            JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            responseBody =gson.toJson(jsonObject);
        }catch (Exception e){ }

        return "url："+url+"\n"
                +"method："+method+"\n"
                +"header："+header.toString()+"\n"
                +"query："+query.toString()+"\n"
                +"body："+body.toString()+"\n"
                +"response："+responseBody+"\n";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getHeader() {
        return header;
    }

    public void setHeader(Map<String, Object> header) {
        this.header = header;
    }

    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
        //get请求param
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
        //post 请求body
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public ReadContext getContext() {
        return this.context;
    }

    public void setContext(Response response) {
        this.context = JsonPath.parse(response.asString());
    }
}
