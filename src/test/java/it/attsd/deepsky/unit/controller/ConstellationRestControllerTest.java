package it.attsd.deepsky.unit.controller;

import it.attsd.deepsky.controller.ConstellationRestController;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.service.ConstellationService;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ConstellationRestController.class)
public class ConstellationRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ConstellationService constellationService;

    private final String ORION = "orion";
    private final String SCORPIUS = "scorpius";

    @Test
    public void testAllConstellationsEmpty() throws Exception {
        this.mvc.perform(get("/api/constellation").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }

    @Test
    public void testAllConstellationsNotEmpty() throws Exception {
        when(constellationService.findAll())
                .thenReturn(Arrays.asList(new Constellation(1L, ORION), new Constellation(2L, SCORPIUS)));

        this.mvc.perform(get("/api/constellation").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1))).andExpect(jsonPath("$[0].name", is(ORION)));

    }

    @Test
    public void testGetConstellationByIdIfExists() throws Exception {
        when(constellationService.findById(1L)).thenReturn(new Constellation(1L, ORION));

        this.mvc.perform(get("/api/constellation/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is(ORION)));

    }

    @Test
    public void testGetConstellationByIdIfNotExists() throws Exception {
        when(constellationService.findById(1L)).thenReturn(null);

        this.mvc.perform(get("/api/constellation/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    public void testGetConstellationByNameIfExists() throws Exception {
        when(constellationService.findByName(ORION)).thenReturn(new Constellation(1L, ORION));

        this.mvc.perform(get("/api/constellation/name/" + ORION)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("name", is(ORION)));
    }

    @Test
    public void testGetConstellationByNameIfNotExists() throws Exception {
        when(constellationService.findByName(ORION)).thenReturn(null);

        this.mvc.perform(get("/api/constellation/name/" + ORION)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    public void testSaveConstellation() throws Exception {
        Constellation orionSaved = new Constellation(1L, ORION);

        when(constellationService.save(new Constellation(ORION))).thenReturn(orionSaved);

        JSONObject body = new JSONObject();
        body.put("name", ORION);
        this.mvc.perform(post("/api/constellation").content(body.toString())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is(ORION)));
    }

    @Test
    public void testUpdateConstellationIfNotExists() throws Exception {
        String orionNameUpdated = ORION + " changed";
        Constellation orionUpdated = new Constellation(1L, orionNameUpdated);

        when(constellationService.updateById(1L, new Constellation(orionNameUpdated))).thenReturn(orionUpdated);

        JSONObject body = new JSONObject();
        body.put("name", orionNameUpdated);
        this.mvc.perform(put("/api/constellation/1").content(body.toString())
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("id", is(1))).andExpect(jsonPath("name", is(orionNameUpdated)));

    }

    @Test
    public void testDeleteConstellationById() throws Exception {
        this.mvc.perform(delete("/api/constellation/1").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

    }

}
