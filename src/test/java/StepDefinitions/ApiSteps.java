package StepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApiSteps {

    String token;
    String barcode;
    Response response;

    @Given("^The client sends credentials to the /token endpoint$")
    public void send_token_request() {
        RestAssured.baseURI = "http://mock.case-study.api";

        response = RestAssured
                .given()
                .header("user", "demoUser")
                .header("pass", "demoPass")
                .post("/token");

        token = response.jsonPath().getString("token");
        writeToFile("output/token.txt", response.asPrettyString());
        System.out.println("Token alındı: " + token);
    }

    @Given("A valid barcode {string}")
    public void set_barcode(String code) {
        this.barcode = code;
    }

    @When("The client requests the invoice with this barcode")
    public void request_invoice() {
        response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .get("/viewInvoice?barcode=" + barcode);

        writeToFile("output/viewInvoice.txt", response.asPrettyString());
    }

    @Given("A valid token and barcode {string}")
    public void set_token_and_barcode(String code) {
        this.barcode = code;
    }

    @When("The client sends the invoice to the API")
    public void send_invoice() {
        Map<String, String> body = new HashMap<>();
        body.put("Barcode", barcode);

        response = RestAssured
                .given()
                .header("token", token)
                .contentType(ContentType.JSON)
                .body(body)
                .post("/sendInvoice");

        writeToFile("output/sendInvoice.txt", response.asPrettyString());
    }

    @Then("The response should be saved to {string}")
    public void save_response(String filename) {
        writeToFile("output/" + filename, response.asPrettyString());
    }

    private void writeToFile(String path, String content) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}