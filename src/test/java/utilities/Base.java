package utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

public class Base {

    private static Response response;

    public static String makeAPIRequest(String url){

        try{
            response = RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .when()
                    .get(url)
                    .then()
                    .extract()
                    .response();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return response.getBody().asString();
    }

    public static void validateResponseCode(int responseCode){
        try{
            if(!(response.getStatusCode() == responseCode))
                Assert.fail("Incorrect status code: Expected status code:- "+responseCode+" Actual status code:- "+response.getStatusCode());


            System.out.println(response.getBody().asString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
