package it.attsd.deepsky;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DeepSkyObjectRestControllerE2E {
    private final String ORION = "orion";

    private final String M42 = "m42";

    @Test
    public void testCreateNewDeepSkyObjectWhenNotExists() throws JSONException {
        Integer constellationId = postConstellation(generateRandomConstellationName());
        Integer deepSkyObjectId = postDeepSkyObject(generateRandomDeepSkyObjectName(), constellationId.intValue());

        assertThat(deepSkyObjectId)
                .isNotNull()
                .isPositive();
    }

    @Test
    public void testCreateNewDeepSkyObjectWhenAlreadyExists() throws JSONException {
        // Create DeepSkyObject through REST
        String constellationName = generateRandomConstellationName();
        String deepSkyObjectName = generateRandomDeepSkyObjectName();
        Integer constellationSavedId = postConstellation(constellationName);
        postDeepSkyObject(deepSkyObjectName, constellationSavedId.intValue());

        // Add a DeepSkyObject with the same name
        JSONObject body = new JSONObject();
        body.put("name", deepSkyObjectName);
        String message = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(body.toString()).when()
                .post("/api/deepskyobject")
                .then()
                .statusCode(500)
                .extract().path("message");

        assertThat(message).isEqualToIgnoringCase("A DeepSkyObject with the same name already exists");
    }

    @Test
    public void testUpdateDeepSkyObject() throws JSONException {
        // Create DeepSkyObject through REST
        String constellationName = generateRandomConstellationName();
        String deepSkyObjectName = generateRandomDeepSkyObjectName();
        Integer constellationSavedId = postConstellation(constellationName);
        Integer deepSkyObjectSavedId = postDeepSkyObject(deepSkyObjectName, constellationSavedId.intValue());

        // Update DeepSkyObject name
        String deepSkyObjectNameChanged = deepSkyObjectName + " changed";

        JSONObject constellationJson = new JSONObject();
        constellationJson.put("id", constellationSavedId.intValue());

        JSONObject body = new JSONObject();
        body.put("name", deepSkyObjectNameChanged);
        body.put("constellation", constellationJson);
        given().contentType(MediaType.APPLICATION_JSON_VALUE).body(body.toString()).when()
                .put("/api/deepskyobject/" + deepSkyObjectSavedId.intValue())
                .then()
                .statusCode(200)
                .body(
                        "id", equalTo(deepSkyObjectSavedId.intValue()),
                        "name", equalTo(deepSkyObjectNameChanged)
                );
    }

    @Test
    public void testDeleteDeepSkyObject() throws JSONException {
        // Create DeepSkyObject through REST
        String constellationName = generateRandomConstellationName();
        String deepSkyObjectName = generateRandomDeepSkyObjectName();
        Integer constellationSavedId = postConstellation(constellationName);
        Integer deepSkyObjectSavedId = postDeepSkyObject(deepSkyObjectName, constellationSavedId.intValue());

        given().when()
                .delete("/api/deepskyobject/" + deepSkyObjectSavedId.intValue())
                .then()
                .statusCode(200);
    }

    private String generateRandomConstellationName() {
        return ORION + "-" + Math.random();
    }

    private String generateRandomDeepSkyObjectName() {
        return M42 + "-" + Math.random();
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

    private Integer postDeepSkyObject(String name, int constellationId) throws JSONException {
        JSONObject constellationBody = new JSONObject();
        constellationBody.put("id", constellationId);

        JSONObject deepSkyObjectBody = new JSONObject();
        deepSkyObjectBody.put("name", name);
        deepSkyObjectBody.put("constellation", constellationBody);

        return given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deepSkyObjectBody.toString())
                .when()
                .post("/api/deepskyobject")
                .then()
                .statusCode(200)
                .extract().path("id");
    }

}
