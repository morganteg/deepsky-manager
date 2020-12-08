package it.attsd.deepsky.unit.controller;

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

import it.attsd.deepsky.controller.DeepSkyObjectWebController;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { DeepSkyObjectWebController.class })
public class DeepSkyObjectWebControllerTest {
	@MockBean
	private ConstellationService constellationService;

	@MockBean
	private DeepSkyObjectService deepSkyObjectService;

	@Autowired
	private MockMvc mvc;

	private final String ORION = "orion";
	private final String SCORPIUS = "scorpius";

	private final String M42 = "m42";
	private final String M43 = "m43";

	private final Constellation orion = new Constellation(1L, ORION);
	private final Constellation scorpius = new Constellation(2L, SCORPIUS);

	private final List<Constellation> constellations = Arrays.asList(orion, scorpius);

	private final List<DeepSkyObject> deepSkyObjects = Arrays.asList(new DeepSkyObject(1L, M42, orion),
			new DeepSkyObject(2L, M43, orion));

	@Test
	public void test_GetDeepSkyObject_ShowsDeepSkyObjects() throws Exception {
		when(constellationService.findAll()).thenReturn(constellations);
		when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

		mvc.perform(get("/deepskyobject")).andExpect(view().name("deepSkyObject/deepSkyObject"))
				.andExpect(model().attribute("deepSkyObject", new DeepSkyObject()))
				.andExpect(model().attribute("deepSkyObjects", deepSkyObjects))
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
		DeepSkyObject m42 = new DeepSkyObject(M42, orion);

		mvc.perform(post("/deepskyobject").param("name", M42).param("constellation.id", String.valueOf(orion.getId()))
//				.param("constellation.name", String.valueOf(orion.getName()))
				.flashAttr("deepskyobject", m42)).andExpect(view().name("redirect:/deepskyobject"));

		verify(deepSkyObjectService).save(m42);
	}

	@Test
	public void test_PostConstellationWithId_ShouldUpdateConstellation() throws Exception {
		Constellation orion = new Constellation(1L, M42);

		mvc.perform(post("/constellation").param("id", "1").param("name", M42).flashAttr("constellation", orion))
				.andExpect(view().name("redirect:/constellation"));

		verify(deepSkyObjectService).updateById(1L, orion);
	}

	@Test
	public void test_EditConstellation_WhenConstellationIsFound() throws Exception {
		Constellation orion = new Constellation(1L, M42);

		when(deepSkyObjectService.findById(1L)).thenReturn(orion);
		when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

		mvc.perform(get("/constellation/modify/1")).andExpect(view().name("constellation/constellation"))
				.andExpect(model().attribute("constellation", orion))
				.andExpect(model().attribute("constellations", deepSkyObjects));
//				.andExpect(model().attribute("message", ""));
	}

	@Test
	public void test_EditConstellation_WhenConstellationIsNotFound() throws Exception {
		when(deepSkyObjectService.findById(1L)).thenReturn(null);
		when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

		mvc.perform(get("/constellation/modify/1")).andExpect(view().name("constellation/constellation"))
				.andExpect(model().attribute("constellation.id", Matchers.nullValue()))
				.andExpect(model().attribute("constellation.name", Matchers.nullValue()))
				.andExpect(model().attribute("constellations", deepSkyObjects))
				.andExpect(model().attribute("message", Matchers.equalToIgnoringCase("Constellation not found")));
	}
}
