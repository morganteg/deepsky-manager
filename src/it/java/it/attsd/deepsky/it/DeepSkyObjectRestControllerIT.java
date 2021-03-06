package it.attsd.deepsky.it;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import it.attsd.deepsky.dto.ConstellationDto;
import it.attsd.deepsky.dto.DeepSkyObjectDto;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.repository.ConstellationRepository;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MySQLContainer;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DeepSkyObjectRestControllerIT {
    @ClassRule
    public static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withExposedPorts(3316);

    String ORION = "orion";

    String M42 = "m42";
    String M43 = "m43";

    @Autowired
    private ConstellationRepository constellationRepository;

    @Autowired
    private DeepSkyObjectRepository deepSkyObjectRepository;

    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @Before
    public void setup() {
        RestAssured.port = port;
        deepSkyObjectRepository.deleteAll();
        deepSkyObjectRepository.flush();
        constellationRepository.deleteAll();
        constellationRepository.flush();
    }

    @Test
    public void testGetAllDeepSkyObjects() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
        DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orionSaved));
        DeepSkyObject m43Saved = deepSkyObjectRepository.save(new DeepSkyObject(M43, orionSaved));
        List<DeepSkyObject> deepSkyObjectsSaved = Arrays.asList(m42Saved, m43Saved);

        List<DeepSkyObject> deepSkyObjectsRetrieved = Arrays.asList(given().when()
                .get("/api/deepskyobject")
                .as(DeepSkyObject[].class));

        assertThat(deepSkyObjectsRetrieved).containsAll(deepSkyObjectsSaved);
    }

    @Test
    public void testGetDeepSkyObjectById() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
        DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orionSaved));

        DeepSkyObject deepSkyObjectRetrieved = given().when()
                .get("/api/deepskyobject/" + m42Saved.getId())
                .as(DeepSkyObject.class);

        assertThat(deepSkyObjectRetrieved).isEqualTo(m42Saved);
    }

    @Test
    public void testGetDeepSkyObjectByName() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
        DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orionSaved));

        DeepSkyObject deepSkyObjectRetrieved = given().when()
                .get("/api/deepskyobject/name/" + M42)
                .as(DeepSkyObject.class);

        assertThat(deepSkyObjectRetrieved).isEqualTo(m42Saved);
    }

    @Test
    public void testSaveDeepSkyObject() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));

        Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new DeepSkyObjectDto(M42, constellationToDto(orionSaved)))
                .when()
                .post("/api/deepskyobject");

        DeepSkyObject m42Saved = response.getBody().as(DeepSkyObject.class);

        DeepSkyObject m42Found = deepSkyObjectRepository.findById(m42Saved.getId()).get();
        assertThat(m42Found).isEqualTo(m42Saved);
    }

    @Test
    public void testUpdateDeepSkyObject() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
        DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orionSaved));

        String nameChanged = M42 + " changed";
        given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new DeepSkyObjectDto(nameChanged, constellationToDto(orionSaved))).when()
                .put("/api/deepskyobject/" + m42Saved.getId())
                .then()
                .statusCode(200)
                .body(
                        "id", equalTo(m42Saved.getId().intValue()),
                        "name", equalTo(nameChanged),
                        "constellation.id", equalTo(orionSaved.getId().intValue())
                );
    }

    @Test
    public void testDeleteDeepSkyObjectById() {
        Constellation orionSaved = constellationRepository.save(new Constellation(ORION));
        DeepSkyObject m42Saved = deepSkyObjectRepository.save(new DeepSkyObject(M42, orionSaved));

        given().when()
                .delete("/api/deepskyobject/" + m42Saved.getId())
                .then()
                .statusCode(200);

        assertThat(deepSkyObjectRepository.findById(m42Saved.getId())).isNotPresent();
    }

    private ConstellationDto constellationToDto(Constellation constellation) {
        return new ConstellationDto(
                constellation.getId(),
                constellation.getName()
        );
    }

}
