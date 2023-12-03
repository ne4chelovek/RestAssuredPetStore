import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class APITests {
    private final int unexisitngPetId = 232323493;

    @BeforeEach
    public void setup(){
        RestAssured.baseURI = "https://petstore.swagger.io/v2/";
    }
    @Test
    public void petNotFoundTestWithAssert(){
        RestAssured.baseURI += "pet/" + unexisitngPetId;

        requestSpecification = RestAssured.given();

        Response response = requestSpecification.get();

        System.out.println("Response: " + response.asPrettyString());

        assertEquals(404,response.statusCode(),"Не тот status code");
        assertEquals("HTTP/1.1 404 Not Found", response.statusLine(),"Не корректная status line");
        assertEquals("Pet not found", response.jsonPath().get("message"),"Не то сообщение об ошибке");

    }
    @Test
    public void petNotFoundTest_BDD(){
        given().when()
                .get(baseURI + "pet/{id}", unexisitngPetId)
                .then()
                .log().all()
                .statusCode(404)
                .statusLine("HTTP/1.1 404 Not Found")
                .body("message", equalTo("Pet not found"))
                .body("type", equalTo("error"));
    }
}
