package hooks;

import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utilities.CommunicationChannels;
import utilities.ExtentManager;
import utilities.ExtentTestManager;
import utilities.TestResult;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static utilities.ExtentManager.getExtent;

public class Hooks {

    public static String errorMsg;
    static List<TestResult> testResults = new ArrayList<>();
    static Duration totalDuration = Duration.ZERO;
    Instant startTime;
    Scenario scenario;

    @AfterAll
    public static void closeReport(){
        getExtent().flush();
        ExtentManager.pdfReport(testResults, totalDuration);

        try{
            String emailBody = "Good day,<br><br>I trust that you are well. Please find attached web automation report.<br><br>" +
                    "<b>Kind regards,<br>Automation Team</b>";
            String reportPath = "Report/Automation-Report.html";
            CommunicationChannels.sendReportViaMail("smathebula@getabyte.co.za","Takealot Web Automation Report",emailBody,reportPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void getTstScneario(Scenario scenario){
        ExtentManager.getInstance();
        this.scenario = scenario;
        String scenarioName = scenario.getName();
        ExtentTest test = getExtent().createTest(scenarioName, "Executing " + scenarioName);
        ExtentTestManager.setTest(test);
        startTime = Instant.now();
    }

    @After
    public void afterTest() {
        Duration duration = Duration.between(startTime, Instant.now());
        totalDuration = totalDuration.plus(duration);

        if (scenario.isFailed())
            testResults.add(new TestResult(scenario.getName(), "FAIL", duration.toString(), errorMsg));
        else
            testResults.add(new TestResult(scenario.getName(), "PASS", duration.toString(), scenario.getStatus().toString()));

    }
}
