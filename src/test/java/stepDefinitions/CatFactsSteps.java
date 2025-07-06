package stepDefinitions;

import utilities.Base;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CatFactsSteps extends Base {

    @Given("^I have the cats endpoint$")
    public void i_have_the_cats_endpoint(){
            // https://alexwohlbruck.github.io/cat-facts/
    }
    @When("^I do a GET request$")
    public void i_do_a_get_request(){
        makeAPIRequest("https://alexwohlbruck.github.io/cat-facts/");
    }
    @Then("^I should get a status code of 200$")
    public void i_should_get_a_status_code_of_200(){
        validateResponseCode(200);
    }
}
