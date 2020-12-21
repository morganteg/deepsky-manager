package it.attsd.deepsky.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ConstellationWebControllerIT {

	@Autowired
	private ConstellationRepository constellationRepository;

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private String baseUrl;

	String ORION = "orion";

	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void testConstellations() {
		Constellation testConstellation = constellationRepository.save(new Constellation(ORION));

		driver.get(baseUrl + "/constellation");

		List<WebElement> constellationRows = driver.findElement(By.id("constellations"))
				.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

		assertThat(constellationRows.size()).isEqualTo(1);
		assertThat(constellationRows.get(0).findElement(By.className("constellation-id")).getText()).isEqualTo("1");
		assertThat(constellationRows.get(0).findElement(By.className("constellation-name")).getText()).isEqualTo(ORION);
		constellationRows.get(0).findElement(By.cssSelector("a[href*='constellation/modify/" + testConstellation.getId() + "']"));
		constellationRows.get(0).findElement(By.cssSelector("a[href*='constellation/delete/" + testConstellation.getId() + "']"));
	}

	@Test
	public void testSaveConstellation() {
		driver.get(baseUrl + "/constellation");

		WebElement nameElement = driver.findElement(By.id("constellationName"));
		nameElement.sendKeys(ORION);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		List<WebElement> constellationRows = driver.findElement(By.id("constellations"))
				.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

//		assertThat(constellationRows.size()).isEqualTo(1);

		assertThat(constellationRows.get(0).findElement(By.className("constellation-id")).getText()).isEqualTo("1");
		assertThat(constellationRows.get(0).findElement(By.className("constellation-name")).getText()).isEqualTo(ORION);
	}

	@Test
	public void testUpdateConstellationIfExists() {
		Constellation testConstellation = constellationRepository.save(new Constellation(ORION));

		driver.get(baseUrl + "/constellation/modify/1");

		String nameChanged = ORION + " changed";

//		driver.findElement(By.id("constellationId")).sendKeys("1");
		WebElement nameElement = driver.findElement(By.id("constellationName"));
		nameElement.clear();
		nameElement.sendKeys(nameChanged);
		driver.findElement(By.id("submitButton")).click();

		List<WebElement> constellationRows = driver.findElement(By.id("constellations"))
				.findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

//		assertThat(constellationRows.size()).isEqualTo(1);

		assertThat(constellationRows.get(0).findElement(By.className("constellation-id")).getText()).isEqualTo(String.valueOf(testConstellation.getId()));
		assertThat(constellationRows.get(0).findElement(By.className("constellation-name")).getText()).isEqualTo(nameChanged);
	}

}
