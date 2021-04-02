package pageObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.PathNotFoundException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;


public class TestStep extends TestCase {
    public String name;
    public Map<String, Object> variables;
    public String description;
    public String testcase;
    protected TestCaseProcess process;
    protected Pattern pattern = Pattern.compile("\\$\\{([0-9a-zA-Z\\-\\_]*)\\}.*");

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public void setTestcase(String testcase) {
        this.testcase = testcase;
    }

    public void setProcess(TestCaseProcess process) {
        this.process = process;
    }

    public TestCaseProcess getProcess() {
        return this.process;
    }

    /*
    * 将testcase加载进来
     */
    private void loadTestCase() throws IOException {
        if(null != testcase) {
            try{
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                TestCase testCase1 = mapper.readValue(TestStep.class.getResourceAsStream(testcase), TestCase.class);

                Request request = testCase1.getRequest();
                if(null != request) {
                    this.request = request;
                }

                Map<String, String> extract = testCase1.getExtract();
                if(null != extract) {
                    this.extract = extract;
                }

                List<String> export = testCase1.getExport();
                if(null != export) {
                    this.export = export;
                }

                if(null != extract && null == export) {
                    Set<String> keys = extract.keySet();
                    Iterator<String> iterator1= keys.iterator();
                    this.export = new ArrayList<>();
                    while (iterator1.hasNext()){
                        this.export.add(iterator1.next().toString());
                    }
                }

                Map<String, Map<String, Object>> validate = testCase1.getValidate();
                if(null != validate) {
                    this.validate = validate;
                }

            } catch (UnrecognizedPropertyException exception) {
                System.out.println(testcase);
                System.out.println(exception.getMessage());
                System.out.println(exception.getStackTrace());
            }
        }
    }

    /*
    * TODO 这种写法太low 封装起来
    * 替换变量
     */
    private void replaceVariables() {
        if(null != variables) {
            for(Map.Entry<String, Object> kv: variables.entrySet()) {
                Matcher matcher = pattern.matcher(kv.getValue().toString());
                if(matcher.find()) {
                    String match = matcher.group(1);
                    if(process.getParams().getParameters().containsKey(match)){
                        process.setParameters(kv.getKey(), process.getParams().getParameters().get(match));
                    }
                }
                else {
                    process.setParameters(kv.getKey(), kv.getValue());
                }
            }
        }
    }

    /**
     * 替换Headers方法
     *
     */
    private void replaceHeaders() {
        if(null != this.request.headers) {
            for (Map.Entry<String, Object> kv : this.request.headers.entrySet()) {
                if(kv.getValue() instanceof String) {
                    Matcher matcher = pattern.matcher(kv.getValue().toString());
                    if(matcher.find()) {
                        String match = matcher.group(1);
                        Object tempParam = this.process.getParams().find(match);
                        if(null != tempParam) {
                            this.request.headers.put(kv.getKey(), tempParam.toString());
                        }
                    }
                } else if( kv.getValue() instanceof Map){
                    for(Map.Entry<String, Object> kmap : ((Map<String, Object>) kv.getValue()).entrySet()) {
                        Matcher matcher = pattern.matcher(kmap.getValue().toString());
                        if(matcher.find()) {
                            Object tempParam = this.process.getParams().find(matcher.group(1));
                            if(null != tempParam) {
                                ((Map<String, Object>) this.request.headers.get(kv.getKey())).put(kmap.getKey(), tempParam.toString());
                                System.out.println("headers的" + kv.getKey() + "的" + kmap.getKey() + "的被替换成" + tempParam.toString());
                            }
                        }
                    }
                } else if( kv.getValue() instanceof List){
                    int size = ((List<Map<String, Object>>) kv.getValue()).size();
                    for(int i = 0; i < size; i++) {
                        Map<String, Object> tempMap = new HashMap<>();
                        for(Map.Entry<String, Object> kList : ((List<Map<String, Object>>) kv.getValue()).get(i).entrySet()) {
                            Matcher matcher = pattern.matcher(kList.getValue().toString());
                            if(matcher.find()) {
                                Object tempParam = this.process.getParams().find(matcher.group(1));
                                if (null != tempParam) {
                                    tempMap.put(kList.getKey(), tempParam);
                                    System.out.println("query的" + kv.getKey() + "的" + i + "个的" + kList.getKey() + "被替换成" + tempParam.toString());
                                }
                            }
                        }
                        if(!tempMap.isEmpty()) {
                            ((List<Map<String, Object>>)this.request.headers.get(kv.getKey())).set(i, tempMap);
                        } else {
                            ((List<Map<String, Object>>)this.request.headers.get(kv.getKey())).remove(i);
                            size --;
                            i--;
                        }
                    }
                }
            }
        }
    }

    /**
     * 替换Json方法
     *
     */
    private void replaceJson() {
        if (null != this.request.json) {
            for (Map.Entry<String, Object> kv : this.request.json.entrySet()) {
                if(kv.getValue() instanceof String) {
                    Matcher matcher = pattern.matcher(kv.getValue().toString());
                    if(matcher.find()) {
                        String match = matcher.group(1);
                        Object tempParam = this.process.getParams().find(match);
                        if(null != tempParam) {
                            this.request.json.put(kv.getKey(), tempParam.toString());
                            System.out.println("json的" + kv.getKey() + "替换成" + tempParam.toString());
                        }
                    }
                } else if( kv.getValue() instanceof Map){
                    System.out.println("Map");
                    for(Map.Entry<String, Object> kmap : ((Map<String, Object>) kv.getValue()).entrySet()) {
                        Matcher matcher = pattern.matcher(kmap.getValue().toString());
                        if(matcher.find()) {
                            Object tempParam = this.process.getParams().find(matcher.group(1));
                            if(null != tempParam) {
                                ((Map<String, Object>) this.request.json.get(kv.getKey())).put(kmap.getKey(), tempParam.toString());
                                System.out.println("json的" + kv.getKey() + "的" + kmap.getKey() + "的被替换成" + tempParam.toString());
                            }
                        }
                    }
                } else if( kv.getValue() instanceof List){
                    int size = ((List<Map<String, Object>>) kv.getValue()).size();
                    for(int i = 0; i < size; i++) {
                        Map<String, Object> tempMap = new HashMap<>();
                        List<String> tempList = new ArrayList<>();
                        if(((List)kv.getValue()).get(i) instanceof String) {
                            Matcher matcher = pattern.matcher(((List)kv.getValue()).get(i).toString());
                            if(matcher.find()) {
                                Object tempParam = this.process.getParams().find(matcher.group(1));
                                if(null != tempParam) {
                                    this.request.json.put(kv.getKey(), new Object[]{tempParam});
                                }
                            }
                        } else {
                            for (Map.Entry<String, Object> kList : ((List<Map<String, Object>>) kv.getValue()).get(i).entrySet()) {
                                Matcher matcher = pattern.matcher(kList.getValue().toString());
                                if (matcher.find()) {
                                    Object tempParam = this.process.getParams().find(matcher.group(1));
                                    if (null != tempParam) {
                                        tempMap.put(kList.getKey(), tempParam);
                                        System.out.println("json的" + kv.getKey() + "的" + i + "个的" + kList.getKey() + "被替换成" + tempParam.toString());
                                    }
                                }
                            }
                            if (!tempMap.isEmpty()) {
                                ((List<Map<String, Object>>) this.request.json.get(kv.getKey())).set(i, tempMap);
                            } else {
                                ((List<Map<String, Object>>) this.request.json.get(kv.getKey())).remove(i);
                                size--;
                                i--;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 替换Query方法
     *
     */
    private void replaceQuery() {
        if (null != this.request.query) {
            for (Map.Entry<String, Object> kv : this.request.query.entrySet()) {
                if(kv.getValue() instanceof String) {
                    Matcher matcher = pattern.matcher(kv.getValue().toString());
                    if(matcher.find()) {
                        String match = matcher.group(1);
                        Object tempParam = this.process.getParams().find(match);
                        if(null != tempParam) {
                            this.request.query.put(kv.getKey(), tempParam.toString());
                        }
                    }
                } else if( kv.getValue() instanceof Map){
                    System.out.println("Map");
                    for(Map.Entry<String, Object> kmap : ((Map<String, Object>) kv.getValue()).entrySet()) {
                        Matcher matcher = pattern.matcher(kmap.getValue().toString());
                        if(matcher.find()) {
                            Object tempParam = this.process.getParams().find(matcher.group(1));
                            if(null != tempParam) {
                                ((Map<String, Object>) this.request.query.get(kv.getKey())).put(kmap.getKey(), tempParam.toString());
                                System.out.println("query的" + kv.getKey() + "的" + kmap.getKey() + "的被替换成" + tempParam.toString());
                            }
                        }
                    }
                } else if( kv.getValue() instanceof List){
                    int size = ((List<Map<String, Object>>) kv.getValue()).size();
                    for(int i = 0; i < size; i++) {
                        Map<String, Object> tempMap = new HashMap<>();
                        for(Map.Entry<String, Object> kList : ((List<Map<String, Object>>) kv.getValue()).get(i).entrySet()) {
                            Matcher matcher = pattern.matcher(kList.getValue().toString());
                            if(matcher.find()) {
                                Object tempParam = this.process.getParams().find(matcher.group(1));
                                if (null != tempParam) {
                                    tempMap.put(kList.getKey(), tempParam);
                                    System.out.println("query的" + kv.getKey() + "的" + i + "个的" + kList.getKey() + "被替换成" + tempParam.toString());
                                }
                            }
                        }
                        if(!tempMap.isEmpty()) {
                            ((List<Map<String, Object>>)this.request.query.get(kv.getKey())).set(i, tempMap);
                        } else {
                            ((List<Map<String, Object>>)this.request.query.get(kv.getKey())).remove(i);
                            size --;
                            i--;
                        }
                    }
                }
            }
        }
    }

    /*
    * 将替换好的内容，发送请求
     */
    private void run() throws IOException {
        Restful restful = this.request.toRequest();
        process.addRestful(this.name, restful);
        if(null != extract) {
            for(Map.Entry<String, String> kv: extract.entrySet()) {
                //TODO 判断是否符合jsonpath
                Object object;
                try{
                    object = restful.getContext().read(kv.getValue());
                } catch (PathNotFoundException exception) {
                    object = null;
                }

                if(kv.getKey().equals("cartIdList")) {
                    System.out.println(object);
                }
                if(object instanceof List ) {
                    if(((List) object).size() > 0) {
                        object = ((List) object).get(0);
                    } else {
                        object = null;
                    }
                }

                //提取后保存在变量里
                process.setParameters(kv.getKey(), object);
            }
        }
    }

    /*
    * 执行断言
     */
    private void validate() {
        if(null != validate) {
            Map<String, Object> parameters = process.getParams().getParameters();
            for (Map.Entry<String, Map<String, Object>> kv : validate.entrySet()) {
                if (kv.getKey().equals("equalTo")) {
                    for (Map.Entry<String, Object> kk : kv.getValue().entrySet()) {
                        switch (((List<Object>) kk.getValue()).get(0).toString()) {
                            case "Integer":
                                assertThat(Integer.parseInt(parameters.get(kk.getKey()).toString()), equalTo(((List<Object>) kk.getValue()).get(1)));
                        }
                    }
                }
                if (kv.getKey().equals("greaterThan")) {
                    for (Map.Entry<String, Object> kk : kv.getValue().entrySet()) {
                        switch (((List<Object>) kk.getValue()).get(0).toString()) {
                            case "Integer":
                                assertThat(parameters.get(kk.getKey()).toString().length(), greaterThan(Integer.parseInt(((List<Object>) kk.getValue()).get(1).toString())));
                        }
                    }
                }
            }
        }
    }

    public void toRunStep() throws IOException {
        loadTestCase();
//        System.out.println("after loadTestCase");
//        process.print();
        replaceVariables();
//        System.out.println("after replaceVariables");
//        process.print();
        replaceHeaders();
        replaceJson();
        replaceQuery();
        run();
//        process.print();
        validate();
    }

}
