package utilities;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CommonFunctions implements ConcurrentEventListener {



    public static Properties readPropertiesFile(String fileName){
        try {
            String path = "src/test/resources/"+fileName+".properties";
            FileInputStream fis = new FileInputStream(path);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();

            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties");
        }
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        try{
            publisher.registerHandlerFor(TestStepFinished.class, this::onStepFinished);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void onStepFinished(TestStepFinished event) {
        try{
            if (event.getTestStep() instanceof PickleStepTestStep) {
                PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
                String keyword = step.getStep().getKeyword();
                String text = step.getStep().getText();
                Result result = event.getResult();

                if (result.getStatus() == Status.PASSED) {
                    ExtentTestManager.getTest().pass(keyword + text);
                } else if (result.getStatus() == Status.FAILED) {
                    ExtentTestManager.getTest().fail(keyword + text);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
