package it.attsd.deepsky.e2e.rest;

import io.github.bonigarcia.wdm.WebDriverManager;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

public class DeepSkyObjectWebControllerE2E {
    private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));
    private static String baseUrl = "http://localhost:" + port;
    private WebDriver driver;

    private final String ORION = "orion";

    String M42 = "m42";
    String M43 = "m43";

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + port;
        driver = new ChromeDriver();
    }

    @After
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testHomePage() {
        driver.get(baseUrl);
        driver.findElement(By.cssSelector("a[href*='/deepskyobject"));
    }

    @Test
    public void testCreateNewDeepSkyObjectWhenNotExists() throws JSONException {
        driver.get(baseUrl);

        postConstellation(ORION);

        driver.findElement(By.cssSelector("a[href*='/deepskyobject")).click();

        driver.findElement(By.id("deepSkyObjectName")).sendKeys(M42);
        new Select(driver.findElement(By.id("constellationId"))).selectByVisibleText(ORION);
        driver.findElement(By.id("submitButton")).click();

        assertThat(driver.findElement(By.id("deepSkyObjects")).getText()).contains(M42);
    }

    @Test
    public void testCreateNewDeepSkyObjectWhenAlreadyExists() throws JSONException {
        driver.get(baseUrl);

        // Create DeepSkyObject through REST
        Constellation constellationSaved = postConstellation(ORION);
        postDeepSkyObject(M42, constellationSaved);

        driver.findElement(By.cssSelector("a[href*='/deepskyobject")).click();

        driver.findElement(By.id("deepSkyObjectName")).sendKeys(M42);
        new Select(driver.findElement(By.id("constellationId"))).selectByVisibleText(ORION);
        driver.findElement(By.id("submitButton")).click();

        assertThat(driver.findElement(By.id("errorMessage")).getText()).isEqualTo("A DeepSkyObject with the same name already exists");
    }

    @Test
    public void testUpdateDeepSkyObject() throws JSONException {
        // Create Constellation through REST
        Constellation constellationSaved = postConstellation(ORION);
        DeepSkyObject deepSkyObjectSaved = postDeepSkyObject(M42, constellationSaved);

        driver.get(baseUrl);

        // Go to /deepskyobject page
        driver.findElement(By.cssSelector("a[href*='/deepskyobject")).click();

        // Go to /deepskyobject/modify/<id> page
        driver.findElement(By.cssSelector("a[href*='/deepskyobject/modify/" + deepSkyObjectSaved.getId())).click();

        String nameChanged = M42 + " changed";

        WebElement constellationNameElement = driver.findElement(By.id("deepSkyObjectName"));
        constellationNameElement.clear();
        new Select(driver.findElement(By.id("constellationId"))).selectByVisibleText(ORION);
        constellationNameElement.sendKeys(nameChanged);

        driver.findElement(By.id("submitButton")).click();

        assertThat(driver.findElement(By.id("deepSkyObjects")).getText()).contains(nameChanged);
    }

    @Test
    public void testDeleteDeepSkyObject() throws JSONException {
        driver.get(baseUrl);

        // Create DeepSkyObject through REST
        Constellation constellationSaved = postConstellation(ORION);
        DeepSkyObject deepSkyObjectSaved = postDeepSkyObject(M42, constellationSaved);

        driver.findElement(By.cssSelector("a[href*='/deepskyobject")).click();
        driver.findElement(By.cssSelector("a[href*='/deepskyobject/delete/" + deepSkyObjectSaved.getId())).click();

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("deepSkyObjects")));
    }

    private Constellation postConstellation(String name) throws JSONException {
        JSONObject body = new JSONObject();
        body.put("name", name);
        Constellation orionSaved = given().contentType(MediaType.APPLICATION_JSON_VALUE).body(body.toString()).when()
                .post("/api/constellation")
                .getBody().as(Constellation.class);

        return orionSaved;
    }

    private DeepSkyObject postDeepSkyObject(String name, Constellation constellation) throws JSONException {
        JSONObject constellationBody = new JSONObject();
        constellationBody.put("id", constellation.getId());
        constellationBody.put("name", constellation.getName());

        JSONObject deepSkyObjectBody = new JSONObject();
        deepSkyObjectBody.put("name", name);
        deepSkyObjectBody.put("constellation", constellationBody);
        DeepSkyObject deepSkyObjectSaved = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deepSkyObjectBody.toString())
                .when()
                .post("/api/deepskyobject").getBody().as(DeepSkyObject.class);

        return deepSkyObjectSaved;
    }

}
