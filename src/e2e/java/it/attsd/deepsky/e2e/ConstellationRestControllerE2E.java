package it.attsd.deepsky.e2e;

import io.restassured.RestAssured;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ConstellationRestControllerE2E {
    private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
    private final String ORION = "orion";

    @Before
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void testCreateNewConstellationWhenNotExists() throws JSONException {
        Integer constellationId = postConstellation(generateRandomConstellationName());

        assertThat(constellationId)
                .isNotNull()
                .isPositive();
    }

    @Test
    public void testCreateNewConstellationWhenAlreadyExists() throws JSONException {
        // Create Constellation through REST
        String name = generateRandomConstellationName();
        int id = postConstellation(name);

        // Add a Constellation with the same name
        JSONObject body = new JSONObject();
        body.put("name", name);
        String message = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(body.toString()).when()
                .post("/api/constellation")
                .then()
                .statusCode(500)
                .extract().path("message");

        assertThat(message).isEqualToIgnoringCase("A Constellation with the same name already exists");
    }

    @Test
    public void testUpdateConstellation() throws JSONException {
        // Create Constellation through REST
        String name = generateRandomConstellationName();
        Integer constellationId = postConstellation(name);

        // Update Constellation name
        String nameChanged = name + " changed";

        JSONObject body = new JSONObject();
        body.put("name", nameChanged);
        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(body.toString()).when()
                .put("/api/constellation/" + constellationId.intValue())
                .then()
                .statusCode(200)
                .body(
                        "id", equalTo(constellationId.intValue()),
                        "name", equalTo(nameChanged)
                );
    }

    @Test
    public void testDeleteConstellation() throws JSONException {
        // Create Constellation through REST
        String name = generateRandomConstellationName();
        Integer constellationId = postConstellation(name);

        given().when()
                .delete("/api/constellation/" + constellationId.intValue())
                .then()
                .statusCode(200);
    }

    private String generateRandomConstellationName() {
        return ORION + "-" + Math.random();
    }

    private Integer postConstellation(String name) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("name", name);
        return given().contentType(MediaType.APPLICATION_JSON_VALUE).body(body.toString()).when()
                .post("/api/constellation")
                .then()
                .statusCode(200)
                .extract().path("id");
    }

}
