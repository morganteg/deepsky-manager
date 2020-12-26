package it.attsd.deepsky.unit.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import it.attsd.deepsky.controller.HomeWebController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HomeWebController.class)
public class HomeWebControllerTest {
	@Autowired
	private MockMvc mvc;
	
	private final String BASE_URL = "/";

	@Test
	public void testReturnHomeView() throws Exception {
		ModelAndViewAssert.assertViewName(mvc.perform(get(BASE_URL))
			.andReturn()
			.getModelAndView(), "index");
	}
}
