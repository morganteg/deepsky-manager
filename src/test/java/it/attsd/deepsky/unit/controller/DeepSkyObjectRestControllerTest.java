package it.attsd.deepsky.unit.controller;

import it.attsd.deepsky.controller.DeepSkyObjectRestController;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.service.DeepSkyObjectService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DeepSkyObjectRestController.class)
public class DeepSkyObjectRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DeepSkyObjectService deepSkyObjectService;

    private String BASE_URL = "/api/deepskyobject";

    private final String ORION = "orion";

    private final String M42 = "m42";
    private final String M43 = "m43";

    private final Constellation orion = new Constellation(1L, ORION);

    private final List<DeepSkyObject> deepSkyObjects = Arrays.asList(new DeepSkyObject(1L, M42, orion),
            new DeepSkyObject(2L, M43, orion));

//    private Gson gson = new Gson();

    @Test
    public void testAllDeepSkyObjectsEmpty() throws Exception {
        this.mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testAllDeepSkyObjectsNotEmpty() throws Exception {
        when(deepSkyObjectService.findAll()).thenReturn(deepSkyObjects);

        this.mvc.perform(get(BASE_URL).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].name", is(M42)))
                .andExpect(jsonPath("$[0].constellation.id", is(1)))
                .andExpect(jsonPath("$[0].constellation.name", is(ORION)));

    }

    @Test
    public void testGetDeepSkyObjectByIdIfExists() throws Exception {
        when(deepSkyObjectService.findById(1L)).thenReturn(new DeepSkyObject(1L, M42, orion));

        this.mvc.perform(get(BASE_URL + "/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is(M42)))
                .andExpect(jsonPath("constellation.id", is(1))).andExpect(jsonPath("constellation.name", is(ORION)));

    }

    @Test
    public void testGetDeepSkyObjectByIdIfNotExists() throws Exception {
        when(deepSkyObjectService.findById(1L)).thenReturn(null);

        this.mvc.perform(get(BASE_URL + "/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    public void testGetDeepSkyObjectByNameIfExists() throws Exception {
        when(deepSkyObjectService.findByName(M42)).thenReturn(new DeepSkyObject(1L, M42, orion));

        this.mvc.perform(get(BASE_URL + "/name/" + M42)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("name", is(M42)))
                .andExpect(jsonPath("constellation.id", is(1)))
                .andExpect(jsonPath("constellation.name", is(ORION)));

    }

    @Test
    public void testGetDeepSkyObjectByNameIfNotExists() throws Exception {
        when(deepSkyObjectService.findByName(M42)).thenReturn(null);

        this.mvc.perform(get(BASE_URL + "/name/" + M42)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    public void testSaveDeepSkyObject() throws Exception {
        DeepSkyObject m42 = new DeepSkyObject(1L, M42, orion);

        when(deepSkyObjectService.save(new DeepSkyObject(M42, orion))).thenReturn(m42);

        JSONObject constellationJson = new JSONObject();
        constellationJson.put("id", orion.getId());
        constellationJson.put("name", orion.getName());

        JSONObject body = new JSONObject();
        body.put("name", M42);
        body.put("constellation", constellationJson);

        this.mvc.perform(post(BASE_URL).content(body.toString())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("name", is(M42)))
                .andExpect(jsonPath("constellation.id", is(1)))
                .andExpect(jsonPath("constellation.name", is(ORION)));

    }

    @Test
    public void testUpdateDeepSkyObjectIfNotExists() throws Exception {
        String m42NameUpdated = M42 + " changed";
        DeepSkyObject m42Updated = new DeepSkyObject(1L, m42NameUpdated, orion);

        when(deepSkyObjectService.updateById(1L, new DeepSkyObject(m42NameUpdated, orion))).thenReturn(m42Updated);

        JSONObject constellationJson = new JSONObject();
        constellationJson.put("id", orion.getId());
        constellationJson.put("name", orion.getName());

        JSONObject body = new JSONObject();
        body.put("name", m42NameUpdated);
        body.put("constellation", constellationJson);

        this.mvc.perform(put(BASE_URL + "/1").content(body.toString())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("name", is(m42NameUpdated)))
                .andExpect(jsonPath("constellation.id", is(1)))
                .andExpect(jsonPath("constellation.name", is(ORION)));

    }

    @Test
    public void testDeleteDeepSkyObjectById() throws Exception {
        this.mvc.perform(delete(BASE_URL + "/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

    }

}
