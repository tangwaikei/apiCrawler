package pageObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    public static TestCaseModel load2(InputStream inputStream) throws IOException {
        //处理config的param
        //传递给testSteps
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TestCaseModel model = new TestCaseModel();
        return mapper.readValue(inputStream, model.getClass());
    }

    public void run(TestCaseProcess process) throws IOException {
        for(TestStep testStep: testSteps) {
            testStep.setProcess(process);
            testStep.toRunStep();
        }
    }
}
