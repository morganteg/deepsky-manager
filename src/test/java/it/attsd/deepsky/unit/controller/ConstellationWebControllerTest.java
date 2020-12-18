package it.attsd.deepsky.unit.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.attsd.deepsky.controller.ConstellationWebController;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ConstellationWebController.class)
public class ConstellationWebControllerTest {
	@MockBean
	private ConstellationService constellationService;

	@Autowired
	private MockMvc mvc;
	
	private final String BASE_URL = "/constellation";

	private final String ORION = "orion";
	private final String SCORPIUS = "scorpius";

	private final List<Constellation> constellations = Arrays.asList(new Constellation(1L, ORION),
			new Constellation(2L, SCORPIUS));

	@Test
	public void test_GetConstellation_ShowsConstellations() throws Exception {
		when(constellationService.findAll()).thenReturn(constellations);

		mvc.perform(get(BASE_URL)).andExpect(view().name("constellation/constellation"))
				.andExpect(model().attribute("constellations", constellations));
	}

//	@Test
//	public void test_ConstellationView_NoConstellations() throws Exception {
//		List<Constellation> constellations = Arrays.asList(new Constellation(1L, ORION),
//				new Constellation(2L, SCORPIUS));
//
//		when(constellationService.findAll()).thenReturn(constellations);
//
//		mvc.perform(get("/constellation")).andExpect(view().name("constellation/constellation"))
//				.andExpect(model().attribute("constellations", constellations));
//	}

	@Test
	public void test_PostConstellationWithoutId_ShouldSaveNewConstellation() throws Exception {
		Constellation orion = new Constellation(ORION);

		mvc.perform(post(BASE_URL).param("name", ORION).flashAttr("constellation", orion))
				.andExpect(view().name("redirect:/constellation"));

		verify(constellationService).save(orion);
	}
	
	@Test
	public void test_PostConstellationWithoutIdAndName_ShouldNotSaveNewConstellation() throws Exception {
		when(constellationService.findAll()).thenReturn(constellations);
		
		Constellation orion = new Constellation(null);

		mvc.perform(post(BASE_URL).flashAttr("constellation", orion))
				.andExpect(view().name("constellation/constellation"))
				.andExpect(model().attribute("error", "Please, fill all mandatory attributes"))
				.andExpect(model().attribute("constellations", constellations));

		verify(constellationService, times(0)).save(orion);
	}

	@Test
	public void test_PostConstellationWithId_ShouldUpdateConstellation() throws Exception {
		Constellation orion = new Constellation(1L, ORION);

		mvc.perform(post(BASE_URL).param("id", "1").param("name", ORION).flashAttr("constellation", orion))
				.andExpect(view().name("redirect:/constellation"));

		verify(constellationService).updateById(1L, orion);
	}

	@Test
	public void test_EditConstellation_WhenConstellationIsFound() throws Exception {
		Constellation orion = new Constellation(1L, ORION);

		when(constellationService.findById(1L)).thenReturn(orion);
		when(constellationService.findAll()).thenReturn(constellations);

		mvc.perform(get(BASE_URL + "/modify/1")).andExpect(view().name("constellation/constellation"))
				.andExpect(model().attribute("constellation", orion))
				.andExpect(model().attribute("constellations", constellations));
//				.andExpect(model().attribute("message", ""));
	}

	@Test
	public void test_EditConstellation_WhenConstellationIsNotFound() throws Exception {
		when(constellationService.findById(1L)).thenReturn(null);
		when(constellationService.findAll()).thenReturn(constellations);

		mvc.perform(get(BASE_URL + "/modify/1"))
				.andExpect(view().name("constellation/constellation"))
				.andExpect(model().attribute("constellation", new Constellation()))
				.andExpect(model().attribute("constellations", constellations))
				.andExpect(model().attribute("error", Matchers.equalToIgnoringCase("Constellation not found")));
	}
	
	@Test
	public void test_DeleteConstellation() throws Exception {
		mvc.perform(get(BASE_URL + "/delete/1")).andExpect(view().name("redirect:/constellation"));
	}
}
