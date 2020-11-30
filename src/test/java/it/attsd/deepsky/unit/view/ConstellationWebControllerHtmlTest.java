package it.attsd.deepsky.unit.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;

import it.attsd.deepsky.controller.ConstellationWebController;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.service.ConstellationService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ConstellationWebController.class)
public class ConstellationWebControllerHtmlTest {
	@Autowired
	private WebClient webClient;

	@MockBean
	private ConstellationService constellationService;

	private final String ORION = "orion";

	@Test
	public void testConstellationPageTitle() throws Exception {
		HtmlPage page = webClient.getPage("/constellation");

		HtmlHeading1 pageTitle = page.getHtmlElementById("pageTitle");
		assertThat(pageTitle.getTextContent()).isEqualTo("Constellations");
	}

	@Test
	public void testNoConstellations() throws Exception {
		HtmlPage page = webClient.getPage("/constellation");

		HtmlDivision emptyMessage = page.getHtmlElementById("emptyMessage");
		assertThat(emptyMessage.getTextContent().trim()).isEqualTo("No constellations");
	}
	
	@Test
	public void testModifyConstellationWhenNotExists() throws Exception {
		when(constellationService.findById(1L)).thenReturn(null);

		HtmlPage page = webClient.getPage("/constellation/modify/1");

		HtmlParagraph message = page.getHtmlElementById("message");
		assertThat(message.getTextContent().trim()).isEqualTo("Constellation not found");
	}

	@Test
	public void testLoadConstellationForModifyWhenExists() throws Exception {
		Constellation constellation = new Constellation(1L, ORION);
		
		when(constellationService.findById(1L)).thenReturn(constellation);

		HtmlPage page = webClient.getPage("/constellation/modify/1");
		final HtmlForm constellationForm = page.getFormByName("constellationForm");

		assertThat(constellationForm.getInputByValue(ORION).getValueAttribute()).isEqualToIgnoringCase(ORION);
	}
	
	@Test
	public void testModifyConstellationWhenExists() throws Exception {
		Constellation constellation = spy(new Constellation(1L, ORION));
		
		when(constellationService.findById(1L)).thenReturn(constellation);

		HtmlPage page = webClient.getPage("/constellation/modify/1");
		final HtmlForm constellationForm = page.getFormByName("constellationForm");

		constellationForm.getInputByValue(ORION).setValueAttribute(ORION + " changed");
		constellationForm.getInputByName("submitButton").click();
		
		InOrder inOrder = inOrder(constellation, constellationService);
		inOrder.verify(constellationService).findById(1L);
		inOrder.verify(constellationService).findAll();
		inOrder.verify(constellation).getId();
		inOrder.verify(constellationService).updateById(1L, constellation);
		inOrder.verify(constellationService).findAll();
//
//		verify(constellationService).findById(1L);
//		verify(constellationService).findAll();
//		verify(constellationService).updateById(1L, constellation);
	}
	

}
