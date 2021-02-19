package base;

import java.util.List;
import java.util.Map;

public class TestCase {
    public String name; //用来存储到数据库
    public NewRequest request;
    public List<String> export;
    public Map<String, String> extract;
    public Map<String, Map<String, Object>> validate;

    public void setName(String name) {
        this.name = name;
    }

    public void setRequest(NewRequest request) {
        this.request = request;
    }

    public void setExport(List<String> export) {
        this.export = export;
    }

    public void setExtract(Map<String, String> extract) {
        this.extract = extract;
    }

    public void setValidate(Map<String, Map<String, Object>> validate) {
        this.validate = validate;
    }

    public NewRequest getRequest() {
        return this.request;
    }

    public List<String> getExport() {
        return this.export;
    }

    public Map<String, String> getExtract() {
        return this.extract;
    }

    public Map<String, Map<String, Object>> getValidate() {
        return this.validate;
    }
}
