# apiCrawler
## 特色
* 使用Junit5 + Rest Assured + Hamcrest + Allure搭建框架
* 以yaml格式定义测试用例模板，实现数据驱动
* 测试用例支持使用“${XX}”格式来代表变量，执行前被替换成对应的值
* 将常用的测试用例变成单元，不可直接执行，但可在测试步骤中多处调用，避免测试用例重复描述
* 支持variables/extract/validate机制，创建复杂的测试用例
* 使用jsonPath来提取和验证json响应体
## 名词解释
* 测试用例单元（TestCase）是一个最小化的接口定义，不能单独执行，需要嵌套在测试步骤内。
* 测试步骤（TestStep）可引用测试用例单元，是一个完整的接口请求响应。
* 测试用例集（TestStepModel）的测试步骤序列是测试步骤（TestStep）的有序集合，测试步骤有顺序或者依赖关系。
* 测试用例集（TestStepModel）之间是无序，形成测试场景，在ApiTest中使用Junit5的测试参数执行。
## 目录结构
```$xslt
src
├── test
│    ├── java
│    	    ├── pageObject
│          	    ├── ApiTest
│          	    ├── Config
│          	    ├── Parameters
│          	    ├── Request
│          	    ├── Restful
│         	    ├── TestCase
│         	    ├── TestCaseProcess
│          	    ├── TestStep
│          	    ├── TestStepModel
│          	    ├── Validate
│    	    ├── util
│          	    ├── function
├── ── resources
│    ├── pageObject
│    	    ├── config
│          	    ├── gloabl.yaml
│    	    ├── module
│          	    ├── testcaseA.yaml
```
## 变量
#### 1. Request
* String url
* String method
* Map<String, Object> headers
* Map<String, Object> json
* Map<String, Object> query
#### 2. Config
* String name
* Map<String, Object> variables
#### 3. TestCase
* String name
* Request request
* List<String> export
* Map<String, String> extract
* Map<String, Map<String, Object>> validate
#### 4. TestStep
* String name
* Map<String, Object> variables
* String testcase
#### 5. TestStepModel
* Config config
* List<TestStep> testSteps
## 核心代码
```java
void toRunStep() {
        loadTestCase();//加载测试用例
        replaceVariables();//替换testcase和config里变量
        replaceHeaders();//替换headers里的变量
        replaceJson();//替换body的json变量
        replaceQuery();//替换body的query变量
        run();//运行并保存变量
        validate();//断言
    }
```
## yaml例子
```yaml
config:
    name: testsuite名字
testSteps:
    -
        name: testStep1名字
        request:
            url: api
            method: get
            headers:
                token: ${token}
                user-agent: ${user-agent}
        extract:    
            paramName: "jsonPath规则提取参数"
        export: [ "paramName"]
    -
        name: testStep2名字
        variables:
            currentPage: 1
            fenceId: ${paramName}
        testcase: "testcase路径"  
        validate:
            greaterThan:
                cartId: ["Integer", 0]
```