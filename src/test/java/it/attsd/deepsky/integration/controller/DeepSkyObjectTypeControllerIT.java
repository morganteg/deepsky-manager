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

import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DeepSkyObjectTypeControllerIT {
	@Autowired
	private ConstellationRepository constellationRepository;

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	@Autowired
	private DeepSkyObjectRepository deepSkyObjectRepository;

	@Autowired
	private DeepSkyObjectTypeService deepSkyObjectTypeService;

	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;

	private final String NEBULA = "nebula";
	private final String GALAXY = "galaxy";

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
	
	private WebElement getInputType() {
		return driver.findElement(By.id("deepSkyObjectTypeName"));
	}

	@Test
	public void testGetDeepSkyObjectTypes() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);
		DeepSkyObjectType galaxy = deepSkyObjectTypeService.save(new DeepSkyObjectType(GALAXY));
		assertNotNull(galaxy);

		driver.get(baseUrl + "/deepskyobjecttype");

		int tdSize = driver.findElement(By.id("deepSkyObjectTypes")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(tdSize).isEqualTo(2);
	}

	@Test
	public void testSaveDeepSkyObjectTypeIfNotExists() throws DeepSkyObjectTypeAlreadyExistsException {
		driver.get(baseUrl + "/deepskyobjecttype");

		WebElement typeElement = getInputType();
		typeElement.sendKeys(NEBULA);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		int tdSize = driver.findElement(By.id("deepSkyObjectTypes")).findElement(By.tagName("tbody"))
				.findElements(By.tagName("tr")).size();

		assertThat(tdSize).isEqualTo(1);
	}

	@Test
	public void testSaveDeepSkyObjectTypeIfAlreadyExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);

		driver.get(baseUrl + "/deepskyobjecttype");

		WebElement typeElement = getInputType();
		typeElement.clear();
		typeElement.sendKeys(NEBULA);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		assertThat(driver.findElement(By.id("errorMessage")).getText())
				.isEqualToIgnoringCase("DeepSkyObjectType already exists"); // TODO centralize constant
	}

	@Test
	public void testUpdateDeepSkyObjectTypeIfExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);

		driver.get(baseUrl + "/deepskyobjecttype/modify/" + nebula.getId());

		String typeChanged = nebula.getType() + " mod";

		WebElement typeElement = getInputType();
		typeElement.clear();
		typeElement.sendKeys(typeChanged);

		WebElement submitButtonElement = driver.findElement(By.id("submitButton"));
		submitButtonElement.click();

		assertThat(driver.findElement(By.id("deepskyobjecttype-type-" + nebula.getId())).getText())
				.isEqualToIgnoringCase(typeChanged);
	}
	
	@Test
	public void testDeleteDeepSkyObjectTypeIfExists() throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType nebula = deepSkyObjectTypeService.save(new DeepSkyObjectType(NEBULA));
		assertNotNull(nebula);

		driver.get(baseUrl + "/deepskyobjecttype/delete/" + nebula.getId());
		
		assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("deepskyobjecttype-type-" + nebula.getId())));
	}
}
