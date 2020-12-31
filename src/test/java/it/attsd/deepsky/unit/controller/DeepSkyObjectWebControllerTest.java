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

import it.attsd.deepsky.controller.DeepSkyObjectWebController;
import it.attsd.deepsky.exceptions.DeepSkyObjectAlreadyExistsException;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {DeepSkyObjectWebController.class})
public class DeepSkyObjectWebControllerTest {
    @MockBean
    private ConstellationService constellationService;

    @MockBean
    private DeepSkyObjectService deepSkyObjectService;

    @Autowired
    private MockMvc mvc;

    private String BASE_URL = "/deepskyobject";

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

        mvc.perform(get(BASE_URL)).andExpect(view().name("deepSkyObject/deepSkyObject"))
                .andExpect(model().attribute("deepSkyObject", new DeepSkyObject()))
                .andExpect(model().attribute("deepSkyObjects", deepSkyObjects))
                .andExpect(model().attribute("constellations", constellations));
    }

    @Test
    public void test_PostDeepSkyObjectWithoutId_ShouldSave() throws Exception {
        DeepSkyObject m42 = new DeepSkyObject(M42, new Constellation(orion.getId(), null));

        mvc.perform(post(BASE_URL)
                .param("name", M42)
                .param("constellation", String.valueOf(orion.getId())))
                .andExpect(view().name("redirect:/deepskyobject"));

        verify(deepSkyObjectService).save(m42);
    }

    @Test
    public void test_PostDeepSkyObjectWithoutName_ShouldNotSave() throws Exception {
        when(constellationService.findAll()).thenReturn(constellations);
        when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

        DeepSkyObject m42 = new DeepSkyObject(null, new Constellation(orion.getId(), null));

        mvc.perform(post(BASE_URL)
                .param("constellation", String.valueOf(orion.getId())))
                .andExpect(model().attribute("error", Matchers.equalToIgnoringCase("Please, fill all mandatory attributes")))
                .andExpect(model().attribute("deepSkyObject", m42))
                .andExpect(model().attribute("deepSkyObjects", deepSkyObjects))
                .andExpect(model().attribute("constellations", constellations))
                .andExpect(view().name("deepSkyObject/deepSkyObject"));

        verify(deepSkyObjectService, times(0)).save(m42);
    }

    @Test
    public void test_PostDeepSkyObjectWithoutConstellation_ShouldNotSave() throws Exception {
        when(constellationService.findAll()).thenReturn(constellations);
        when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

        DeepSkyObject m42 = new DeepSkyObject(M42, new Constellation());

        mvc.perform(post(BASE_URL)
                .param("name", M42))
                .andExpect(model().attribute("error", Matchers.equalToIgnoringCase("Please, fill all mandatory attributes")))
                .andExpect(model().attribute("deepSkyObject", m42))
                .andExpect(model().attribute("deepSkyObjects", deepSkyObjects))
                .andExpect(model().attribute("constellations", constellations))
                .andExpect(view().name("deepSkyObject/deepSkyObject"));

        verify(deepSkyObjectService, times(0)).save(m42);
    }

    @Test
    public void test_PostDeepSkyObjectWithConstellationIdZero_ShouldNotSave() throws Exception {
        when(constellationService.findAll()).thenReturn(constellations);
        when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

        DeepSkyObject m42 = new DeepSkyObject(M42, new Constellation(0L, null));

        mvc.perform(post(BASE_URL)
                .param("constellation", "0")
                .param("name", M42))
                .andExpect(model().attribute("error", Matchers.equalToIgnoringCase("Please, fill all mandatory attributes")))
                .andExpect(model().attribute("deepSkyObject", m42))
                .andExpect(model().attribute("deepSkyObjects", deepSkyObjects))
                .andExpect(model().attribute("constellations", constellations))
                .andExpect(view().name("deepSkyObject/deepSkyObject"));

        verify(deepSkyObjectService, times(0)).save(m42);
    }

    @Test
    public void test_PostDeepSkyObjectWithoutNameAndConstellation_ShouldNotSave() throws Exception {
        when(constellationService.findAll()).thenReturn(constellations);
        when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

        DeepSkyObject m42 = new DeepSkyObject(null, new Constellation());

        mvc.perform(post(BASE_URL))
                .andExpect(model().attribute("error", Matchers.equalToIgnoringCase("Please, fill all mandatory attributes")))
                .andExpect(model().attribute("deepSkyObject", m42))
                .andExpect(model().attribute("deepSkyObjects", deepSkyObjects))
                .andExpect(model().attribute("constellations", constellations))
                .andExpect(view().name("deepSkyObject/deepSkyObject"));

        verify(deepSkyObjectService, times(0)).save(m42);
    }

    @Test
    public void test_PostDeepSkyObjectAlreadyExisting_ShouldNotSave() throws Exception {
        DeepSkyObject m42 = new DeepSkyObject(M42, new Constellation(orion.getId(), null));

        when(deepSkyObjectService.save(m42)).thenThrow(DeepSkyObjectAlreadyExistsException.class);

        mvc.perform(post(BASE_URL)
                .param("name", M42)
                .param("constellation", String.valueOf(orion.getId())))
                .andExpect(view().name("deepSkyObject/deepSkyObject"))
                .andExpect(model().attribute("error", Matchers.equalToIgnoringCase("A DeepSkyObject with the same name already exists")));

        verify(deepSkyObjectService).save(m42);
    }

    @Test
    public void test_PostDeepSkyObjectWithoutIdAndName_ShouldNotSaveNewDeepSkyObject() throws Exception {
        when(constellationService.findAll()).thenReturn(constellations);
        when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

        DeepSkyObject m42 = new DeepSkyObject(null, new Constellation(orion.getId(), null));

        mvc.perform(post(BASE_URL)
                .param("constellation", String.valueOf(orion.getId())))
                .andExpect(view().name("deepSkyObject/deepSkyObject"))
                .andExpect(model().attribute("error", "Please, fill all mandatory attributes"))
                .andExpect(model().attribute("deepSkyObject", m42))
                .andExpect(model().attribute("deepSkyObjects", deepSkyObjects))
                .andExpect(model().attribute("constellations", constellations));

        verify(deepSkyObjectService, times(0)).save(m42);
    }

    @Test
    public void test_PostDeepSkyObjectWithId_ShouldUpdateDeepSkyObject() throws Exception {
        DeepSkyObject m42 = new DeepSkyObject(1L, M42, new Constellation(orion.getId(), null));

        mvc.perform(post(BASE_URL)
                .param("id", "1")
                .param("name", M42)
                .param("constellation", String.valueOf(orion.getId())))
                .andExpect(view().name("redirect:/deepskyobject"));

        verify(deepSkyObjectService).updateById(1L, m42);
    }

    @Test
    public void test_EditDeepSkyObject_WhenDeepSkyObjectIsFound() throws Exception {
        DeepSkyObject m42 = new DeepSkyObject(1L, M42, orion);

        when(deepSkyObjectService.findById(1L)).thenReturn(m42);
        when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

        mvc.perform(get(BASE_URL + "/modify/1"))
                .andExpect(view().name("deepSkyObject/deepSkyObject"))
                .andExpect(model().attribute("deepSkyObject", m42))
                .andExpect(model().attribute("deepSkyObjects", deepSkyObjects));
    }

    @Test
    public void test_EditDeepSkyObject_WhenDeepSkyObjectIsNotFound() throws Exception {
        when(deepSkyObjectService.findById(1L)).thenReturn(null);
        when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

        mvc.perform(get(BASE_URL + "/modify/1"))
                .andExpect(view().name("deepSkyObject/deepSkyObject"))
                .andExpect(model().attribute("deepSkyObject", new DeepSkyObject()))
                .andExpect(model().attribute("deepSkyObjects", deepSkyObjects))
                .andExpect(model().attribute("error", Matchers.equalToIgnoringCase("Deep-Sky object not found")));
    }

    @Test
    public void test_DeleteDeepSkyObject() throws Exception {
        mvc.perform(get(BASE_URL + "/delete/1")).andExpect(view().name("redirect:/deepskyobject"));
    }
}
