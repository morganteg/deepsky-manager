package it.attsd.deepsky.controller;

import it.attsd.deepsky.exceptions.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exceptions.ConstellationIsStillUsedException;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ConstellationWebController.class)
public class ConstellationWebControllerTest {
    @MockBean
    private ConstellationService constellationService;

    @MockBean
    private DeepSkyObjectService deepSkyObjectService;

    @Autowired
    private MockMvc mvc;

    private final String BASE_URL = "/constellation";

    private final String ORION = "orion";
    private final String SCORPIUS = "scorpius";

    private final String M42 = "m42";
    private final String M43 = "m43";

    private final Constellation orionSaved = new Constellation(1L, ORION);
    private final Constellation scorpiusSaved = new Constellation(2L, SCORPIUS);
    private final List<Constellation> constellations = Arrays.asList(orionSaved, scorpiusSaved);

    private final List<DeepSkyObject> deepSkyObjects = Arrays.asList(new DeepSkyObject(1L, M42, orionSaved),
            new DeepSkyObject(2L, M43, orionSaved));

    @Test
    public void test_GetConstellation_ShowsConstellations() throws Exception {
        when(constellationService.findAll()).thenReturn(constellations);

        mvc.perform(get(BASE_URL)).andExpect(view().name("constellation/constellation"))
                .andExpect(model().attribute("constellations", constellations));
    }

    @Test
    public void test_PostConstellationWithoutId_ShouldSaveNewConstellation() throws Exception {
        Constellation orion = new Constellation(ORION);

        mvc.perform(post(BASE_URL).param("name", ORION))
                .andExpect(view().name("redirect:/constellation"));

        verify(constellationService).save(orion);
    }

    @Test
    public void test_PostConstellationWithoutIdAndName_ShouldNotSaveNewConstellation() throws Exception {
        when(constellationService.findAll()).thenReturn(constellations);

        Constellation orion = new Constellation(null);

        mvc.perform(post(BASE_URL))
                .andExpect(view().name("constellation/constellation"))
                .andExpect(model().attribute("error", "Please, fill all mandatory attributes"))
                .andExpect(model().attribute("constellations", constellations));

        verify(constellationService, times(0)).save(orion);
    }

    @Test
    public void test_PostConstellationAlreadyExisting_ShouldNotSave() throws Exception {
        Constellation orion = new Constellation(ORION);

        when(constellationService.save(orion)).thenThrow(ConstellationAlreadyExistsException.class);

        mvc.perform(post(BASE_URL).param("name", ORION))
                .andExpect(view().name("constellation/constellation"))
                .andExpect(model().attribute("error", Matchers.equalToIgnoringCase("A Constellation with the same name already exists")));

        verify(constellationService).save(orion);
    }

    @Test
    public void test_PostConstellationWithId_ShouldUpdateConstellation() throws Exception {
        Constellation orion = new Constellation(1L, ORION);

        mvc.perform(post(BASE_URL)
                .param("id", "1")
                .param("name", ORION))
                .andExpect(view().name("redirect:/constellation"));

        verify(constellationService).updateById(1L, orion);
    }

    @Test
    public void test_EditConstellation_WhenConstellationIsFound() throws Exception {
        Constellation orion = new Constellation(1L, ORION);

        when(constellationService.findById(1L)).thenReturn(orion);
        when(constellationService.findAll()).thenReturn(constellations);

        mvc.perform(get(BASE_URL + "/modify/1"))
                .andExpect(view().name("constellation/constellation"))
                .andExpect(model().attribute("constellation", orion))
                .andExpect(model().attribute("constellations", constellations));
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

    @Test
    public void test_DeleteUsedConstellation_ShouldNotDelete() throws Exception {
        when(constellationService.findById(1L)).thenReturn(orionSaved);
        when(deepSkyObjectService.findByConstellation(orionSaved)).thenReturn(deepSkyObjects);
        doThrow(new ConstellationIsStillUsedException()).when(constellationService).deleteById(orionSaved.getId());

        mvc.perform(get(BASE_URL + "/delete/" + orionSaved.getId()))
                .andExpect(view().name("constellation/constellation"))
                .andExpect(model().attribute("error", Matchers.equalToIgnoringCase("The Constellation is used by a DeepSkyObject")));

        verify(constellationService).deleteById(orionSaved.getId());
    }
}
