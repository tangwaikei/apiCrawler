package base;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

public class ApiTest {

    @ParameterizedTest(name = "{index} {1}")
    @MethodSource
    void apiTest() {


    }

    static List<Arguments> loadTestCases() {
        List<Arguments> testCases = new ArrayList<>();

        return testCases;
    }
}
