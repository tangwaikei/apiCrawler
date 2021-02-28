package pageObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TestStepModel {
    public Config config;
    public List<TestStep> testSteps;

    public static TestStepModel load(InputStream inputStream) throws IOException {
        //处理config的param
        //传递给testSteps
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TestStepModel model = new TestStepModel();
        return mapper.readValue(inputStream, model.getClass());
    }

    public void run(TestCaseProcess process) throws IOException {
        for(TestStep testStep: testSteps) {
            testStep.setProcess(process);
            testStep.toRunStep();
        }
    }
}
