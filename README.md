# apiCrawler
## 特色
* 以yaml格式定义测试用例，数据驱动
## 名词解释
* 测试用例单元（TestCase）是一个抽象化的接口请求，不能单独执行，嵌套在测试步骤内。
* 测试步骤（TestStep）对应一个接口的请求响应。
* 测试用例集（TestStepModel）的测试步骤序列是测试步骤（TestStep）的有序集合，测试步骤有顺序或者依赖关系。
* 测试用例集（TestStepModel）之间是无序，在ApiTest中使用Junit5的测试参数执行。