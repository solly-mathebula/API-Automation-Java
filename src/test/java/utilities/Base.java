package utilities;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class Base {

    private static Response response;
    private static String URL;
    private static RequestSpecification request;

    public static void getURL(String name) {
        try {
            URL = CommonFunctions.readPropertiesFile("URL").getProperty(name.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String makeAPIRequest(String requestType, String body, Map<String, String> headers) {

        try{
            Method method = Method.valueOf(requestType.toUpperCase());

            // Build request
            request = RestAssured
                    .given()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json");

            // Add optional headers
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.header(entry.getKey(), entry.getValue());
                }
            }

            // Add request body (for POST, PUT, etc.)
            if (body != null && !body.isEmpty()) {
                request.body(body);
            }

            // Send request
            response = request.request(method, URL);
            ExtentTestManager.getTest().info("Request URL: " + URL);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return response.getBody().asString();
    }

    public static String getAuthToken(AuthType authType, String authUrl, Map<String, String> creds) {
        try {
            switch (authType) {
                case LOGIN_JSON: {
                    String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}",
                            creds.get("username"), creds.get("password"));

                    Response r = RestAssured.given()
                            .header("Content-Type", "application/json")
                            .header("Accept", "application/json")
                            .body(body)
                            .request(Method.POST, authUrl);

                    if (r.statusCode() != 200)
                        throw new RuntimeException("Login failed: " + r.statusLine());

                    // allow custom token field (defaults to "token")
                    String field = creds.getOrDefault("tokenField", "token");
                    return r.jsonPath().getString(field);
                }


                case BASIC: {
                    String combo = creds.get("username") + ":" + creds.get("password");
                    String base64 = Base64.getEncoder()
                            .encodeToString(combo.getBytes(StandardCharsets.UTF_8));
                    return "Basic " + base64;
                }

                case OAUTH_CLIENT: {
                    Response r = RestAssured.given()
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .formParam("grant_type", "client_credentials")
                            .formParam("client_id", creds.get("clientId"))
                            .formParam("client_secret", creds.get("clientSecret"))
                            .formParam("scope", creds.getOrDefault("scope", ""))
                            .post(authUrl);

                    if (r.statusCode() != 200)
                        throw new RuntimeException("OAuth token call failed: " + r.statusLine());

                    return "Bearer " + r.jsonPath().getString("access_token");
                }

                case API_KEY:
                    return creds.get("apiKey");

                default:
                    throw new IllegalArgumentException("Unsupported auth type: " + authType);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void validateResponseCode(int responseCode){
        try{
            if(!(response.getStatusCode() == responseCode))
                Assert.fail("Incorrect status code: Expected status code:- "+responseCode+" Actual status code:- "+response.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void validateJsonSchemaExample() {
        RestAssured
                .given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get("/posts/1")
                .then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/user-schema.json"));
    }

    public enum AuthType {LOGIN_JSON, BASIC, OAUTH_CLIENT, API_KEY}
}
