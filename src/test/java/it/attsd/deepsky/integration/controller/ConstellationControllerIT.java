package it.attsd.deepsky.integration.controller;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.model.ConstellationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ConstellationControllerIT {
	@Autowired
	private ConstellationRepository constellationRepository;

	@LocalServerPort
	private int port;
	private WebDriver driver;
	private String baseUrl;
	
	@Before
	public void setup() {
		baseUrl = "http://localhost:" + port;
		driver = new HtmlUnitDriver();
		// always start with an empty database
		constellationRepository.emptyTable();
	}

	@After
	public void teardown() {
		driver.quit();
	}

	@Test
	public void testHomePage() throws ConstellationAlreadyExistsException {
//		Constellation testEmployee = constellationRepository.save(new Constellation(ORION));
		driver.get(baseUrl);
		
		assertThat(driver.findElement(By.id("title")).getText().toLowerCase()).contains("home");
	}
}
