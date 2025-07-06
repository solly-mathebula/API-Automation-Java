package utilities;

import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.Scenario;

import java.util.ArrayList;
import java.util.List;

public class ExtentTestManager {
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final ThreadLocal<List<ExtentTest>> stepNodes = new ThreadLocal<>();

    public static Scenario getScenario;

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void setTest(ExtentTest test) {
        extentTest.set(test);
        stepNodes.set(new ArrayList<>());
    }
}
