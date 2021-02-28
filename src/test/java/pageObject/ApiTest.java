package pageObject;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

/**
 * 测试的入口
 */
public class ApiTest {

    @ParameterizedTest
    @MethodSource("loadTestCases")
    void apiTest(InputStream inputStream) throws IOException {
        if(null != inputStream) {
            String env = "pro";//System.getenv("env");
            TestCaseProcess process = new TestCaseProcess(env);
            TestStepModel testCaseModel = TestStepModel.load(inputStream);
            testCaseModel.run(process);
        }
    }

    /**
     * 将testcase作为测试参数
     * @return
     */
    static Stream<Arguments> loadTestCases() {
        return Stream.of(
                Arguments.arguments(ApiTest.class.getResourceAsStream("shop\\shopCrawler.yaml")),
                Arguments.arguments(ApiTest.class.getResourceAsStream("shoppingCart\\shoppingCartCalc.yaml")),
                Arguments.arguments(ApiTest.class.getResourceAsStream("shoppingCart\\delShoppingCart.yaml")),
                Arguments.arguments(ApiTest.class.getResourceAsStream("home\\homeSelective.yaml")),
                Arguments.arguments(ApiTest.class.getResourceAsStream("shop\\goodsTypeCrawler.yaml"))
        );
    }
}
