package it.attsd.deepsky.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class DeepSkyObjectWebControllerE2E {
    private static int port = Integer.parseInt(System.getProperty("server.port", "8080"));

    private static String baseUrl;
    private WebDriver driver;

    private final String ORION = "orion";

    String M42 = "m42";

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
        WebElement urlElement = driver.findElement(By.cssSelector("a[href*='/deepskyobject"));

        assertThat(urlElement).isNotNull();
    }

    @Test
    public void testCreateNewDeepSkyObjectWhenNotExists() throws JSONException {
        String constellationName = generateRandomConstellationName();
        String deepSkyObjectName = generateRandomDeepSkyObjectName();
        postConstellation(constellationName);

        driver.get(baseUrl);

        driver.findElement(By.cssSelector("a[href*='/deepskyobject")).click();

        driver.findElement(By.id("deepSkyObjectName")).sendKeys(deepSkyObjectName);
        new Select(driver.findElement(By.id("constellationId"))).selectByVisibleText(constellationName);
        driver.findElement(By.id("submitButton")).click();

        assertThat(driver.findElement(By.id("deepSkyObjects")).getText()).contains(deepSkyObjectName);
    }

    @Test
    public void testCreateNewDeepSkyObjectWhenAlreadyExists() throws JSONException {
        // Create DeepSkyObject through REST
        String constellationName = generateRandomConstellationName();
        String deepSkyObjectName = generateRandomDeepSkyObjectName();
        Integer constellationSavedId = postConstellation(constellationName);
        postDeepSkyObject(deepSkyObjectName, constellationSavedId.intValue());

        driver.get(baseUrl);

        driver.findElement(By.cssSelector("a[href*='/deepskyobject")).click();

        driver.findElement(By.id("deepSkyObjectName")).sendKeys(deepSkyObjectName);
        new Select(driver.findElement(By.id("constellationId"))).selectByVisibleText(constellationName);
        driver.findElement(By.id("submitButton")).click();

        assertThat(driver.findElement(By.id("errorMessage")).getText()).isEqualTo("A DeepSkyObject with the same name already exists");
    }

    @Test
    public void testUpdateDeepSkyObject() throws JSONException {
        // Create Constellation through REST
        String constellationName = generateRandomConstellationName();
        String deepSkyObjectName = generateRandomDeepSkyObjectName();
        Integer constellationSavedId = postConstellation(constellationName);
        Integer deepSkyObjectSavedId = postDeepSkyObject(deepSkyObjectName, constellationSavedId.intValue());

        driver.get(baseUrl);

        // Go to /deepskyobject page
        driver.findElement(By.cssSelector("a[href*='/deepskyobject")).click();

        // Go to /deepskyobject/modify/<id> page
        driver.findElement(By.cssSelector("a[href*='/deepskyobject/modify/" + deepSkyObjectSavedId.intValue())).click();

        String nameChanged = deepSkyObjectName + " changed";

        WebElement constellationNameElement = driver.findElement(By.id("deepSkyObjectName"));
        constellationNameElement.clear();
        new Select(driver.findElement(By.id("constellationId"))).selectByVisibleText(constellationName);
        constellationNameElement.sendKeys(nameChanged);

        driver.findElement(By.id("submitButton")).click();

        assertThat(driver.findElement(By.id("deepSkyObjects")).getText()).contains(nameChanged);
    }

    @Test
    public void testDeleteDeepSkyObject() throws JSONException {
        // Create DeepSkyObject through REST
        String constellationName = generateRandomConstellationName();
        String deepSkyObjectName = generateRandomDeepSkyObjectName();
        Integer constellationSavedId = postConstellation(constellationName);
        Integer deepSkyObjectSavedId = postDeepSkyObject(deepSkyObjectName, constellationSavedId.intValue());

        driver.get(baseUrl);

        driver.findElement(By.cssSelector("a[href*='/deepskyobject")).click();
        driver.findElement(By.cssSelector("a[href*='/deepskyobject/delete/" + deepSkyObjectSavedId.intValue())).click();

        assertThat(driver.findElement(By.id("deepSkyObjects")).getText()).doesNotContain(deepSkyObjectName);
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
                .post(baseUrl + "/api/constellation")
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
                .post(baseUrl + "/api/deepskyobject")
                .then()
                .statusCode(200)
                .extract().path("id");
    }

}
