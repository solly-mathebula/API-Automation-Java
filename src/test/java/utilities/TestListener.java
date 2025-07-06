package utilities;

import hooks.Hooks;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {

    }

    @Override
    public void onTestStart(ITestResult result) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().pass("Test passed");

    }

    @Override
    public void onTestFailure(ITestResult result) {
        Hooks.errorMsg = result.getThrowable().getMessage();
        ExtentTestManager.getTest().fail(result.getThrowable().getMessage());

    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {

    }
}
