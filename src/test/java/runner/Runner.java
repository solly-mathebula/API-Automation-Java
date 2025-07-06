package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Listeners;
import utilities.TestListener;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepDefinitions", "utilities", "hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-html-report",
                "json:target/cucumber.json"
        },
        monochrome = true
)

@Listeners(TestListener.class)
public class Runner extends AbstractTestNGCucumberTests {

}