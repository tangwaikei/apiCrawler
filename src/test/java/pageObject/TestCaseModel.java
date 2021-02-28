package pageObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TestCaseModel {
    public Config config;
    public List<TestStep> testSteps;

    public static TestCaseModel load(String path) throws IOException {
        //处理config的param
        //传递给testSteps
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TestCaseModel model = new TestCaseModel();
        return mapper.readValue(new File(path), model.getClass());
    }

    public void run(TestCaseProcess process) throws IOException {
        for(TestStep testStep: testSteps) {
            testStep.setProcess(process);
            testStep.toRunStep();
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> pathList = Arrays.asList(
                "D:\\Java\\code\\apiCrawler\\src\\test\\resources\\shop\\shopCrawler.yaml",
                "D:\\Java\\code\\apiCrawler\\src\\test\\resources\\shoppingCart\\shoppingCartCalc.yaml",
                "D:\\Java\\code\\apiCrawler\\src\\test\\resources\\shoppingCart\\delShoppingCart.yaml",
                "D:\\Java\\code\\apiCrawler\\src\\test\\resources\\home\\homeSelective.yaml",
                "D:\\Java\\code\\apiCrawler\\src\\test\\resources\\shop\\goodsTypeCrawler.yaml"
        );
        String env = "pro";//System.getenv("env");
        TestCaseProcess process = new TestCaseProcess(env);
        for (String path: pathList) {
            TestCaseModel testCaseModel = TestCaseModel.load(path);
            testCaseModel.run(process);
        }
    }
}
