package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utilities.Base;

public class CommonSteps extends Base {

    @Given("^I have the (.*) endpoint$")
    public void i_have_the_cats_endpoint(String urlName) {
        getURL(urlName);
    }

    @When("^I do a (.*) request$")
    public void i_do_a_get_request(String requestType) {
        makeAPIRequest(requestType, null, null);
    }

    @Then("^I should get a status code of (.*)$")
    public void i_should_get_a_status_code_of_200(int code) {
        validateResponseCode(code);
    }
}
