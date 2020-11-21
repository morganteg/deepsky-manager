package it.attsd.deepsky.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ConstellationControllerIT {
	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@Autowired
	private ConstellationService constellationService;

	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;

	private final String ORION = "orion";
	private final String SCORPIUS = "scorpius";

	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver();

		deepSkyObjectRepository.emptyTable();
		constellationRepository.emptyTable();
		deepSkyObjectTypeRepository.emptyTable();
	}

	@After
	public void teardown() {
		driver.quit();
	}
	
	private WebElement getInputName() {
		return driver.findElement(By.id("constellationName"));
	}

	@Test
	public void testGetConstellations() throws ConstellationAlreadyExistsException {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);
		Constellation scorpius = constellationService.save(new Constellation(SCORPIUS));
		assertNotNull(scorpius);

		driver.get(baseUrl + "/constellation");

		int tdSize = driver.findElement(By.id("constellations")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(tdSize).isEqualTo(2);
	}

	@Test
	public void testSaveConstellationIfNotExists() throws ConstellationAlreadyExistsException {
		driver.get(baseUrl + "/constellation");

		WebElement nameElement = getInputName();
		nameElement.sendKeys(ORION);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		int tdSize = driver.findElement(By.id("constellations")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(tdSize).isEqualTo(1);
	}
	
	@Test
	public void testSaveConstellationIfNotExistsWithoutName() throws ConstellationAlreadyExistsException {
		driver.get(baseUrl + "/constellation");

		WebElement nameElement = getInputName();
		nameElement.sendKeys("");

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		int tdSize = driver.findElement(By.id("constellations")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(tdSize).isZero();
	}

	@Test
	public void testSaveConstellationIfAlreadyExists() throws ConstellationAlreadyExistsException {
		Constellation orion = constellationService.save(new Constellation(ORION));
		assertNotNull(orion);

		driver.get(baseUrl + "/constellation");

		WebElement nameElement = getInputName();
		nameElement.clear();
		nameElement.sendKeys(ORION);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		assertThat(driver.findElement(By.id("errorMessage")).getText())
				.isEqualToIgnoringCase("constellation already exists"); // TODO centralize constant
	}

	@Test
	public void testUpdateConstellationIfExists() throws ConstellationAlreadyExistsException {
		Constellation constellation = constellationService.save(new Constellation(ORION));
		assertNotNull(constellation);

		driver.get(baseUrl + "/constellation/modify/" + constellation.getId());

		String nameChanged = constellation.getName() + " mod";

		WebElement nameElement = getInputName();
		nameElement.clear();
		nameElement.sendKeys(nameChanged);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		assertThat(driver.findElement(By.id("constellation-name-" + constellation.getId())).getText())
				.isEqualToIgnoringCase(nameChanged);
	}
	
	@Test
	public void testDeleteConstellationIfExists() throws ConstellationAlreadyExistsException {
		Constellation constellation = constellationService.save(new Constellation(ORION));
		assertNotNull(constellation);

		driver.get(baseUrl + "/constellation/delete/" + constellation.getId());
		
		By byId = By.id("constellation-id-" + constellation.getId());
		
		assertThrows(NoSuchElementException.class, () -> driver.findElement(byId));
	}
}
