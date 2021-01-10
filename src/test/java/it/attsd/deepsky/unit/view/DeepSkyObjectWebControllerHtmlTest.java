package it.attsd.deepsky.unit.view;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import it.attsd.deepsky.controller.DeepSkyObjectWebController;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = DeepSkyObjectWebController.class)
public class DeepSkyObjectWebControllerHtmlTest {
    @Autowired
    private WebClient webClient;

    @MockBean
    private ConstellationService constellationService;

    @MockBean
    private DeepSkyObjectService deepSkyObjectService;

    private final String ORION = "orion";
    private final String M42 = "m42";

    @Test
    public void testDeepSkyObjectPageTitle() throws Exception {
        HtmlPage page = webClient.getPage("/deepskyobject");

        HtmlHeading1 pageTitle = page.getHtmlElementById("pageTitle");
        assertThat(pageTitle.getTextContent()).isEqualTo("DeepSky Objects");
    }

    @Test
    public void testNoDeepSkyObjects() throws Exception {
        HtmlPage page = webClient.getPage("/deepskyobject");

		HtmlTable deepSkyObjectsTable = page.getHtmlElementById("deepSkyObjects");
		assertThat(deepSkyObjectsTable.getBodies().get(0).getTextContent().trim()).isEmpty();
    }

    @Test
    public void testSaveDeepSkyObject() throws Exception {
        Constellation orion = new Constellation(1L, ORION);
        when(constellationService.findAll()).thenReturn(Arrays.asList(
                orion
        ));
		when(deepSkyObjectService.findByName(M42)).thenReturn(null);

        HtmlPage page = webClient.getPage("/deepskyobject");

        final HtmlForm deepSkyObjectForm = page.getFormByName("deepSkyObjectForm");
        deepSkyObjectForm.getInputByName("name").setValueAttribute(M42);
        deepSkyObjectForm.getSelectByName("constellation").setSelectedAttribute("1", true);
        deepSkyObjectForm.getButtonByName("submitButton").click();

		InOrder inOrder = inOrder(constellationService, deepSkyObjectService);
		inOrder.verify(deepSkyObjectService).findAll();
		inOrder.verify(constellationService).findAll();
		inOrder.verify(deepSkyObjectService).save(any(DeepSkyObject.class));
		inOrder.verify(deepSkyObjectService).findAll();
		inOrder.verify(constellationService).findAll();
    }

	@Test
	public void testModifyDeepSkyObjectWhenNotExists() throws Exception {
		when(constellationService.findById(1L)).thenReturn(null);

		HtmlPage page = webClient.getPage("/deepskyobject/modify/1");

		HtmlParagraph message = page.getHtmlElementById("errorMessage");
		assertThat(message.getTextContent().trim()).isEqualTo("Deep-Sky object not found");
	}

	@Test
	public void testLoadDeepSkyObjectForModifyWhenExists() throws Exception {
		Constellation orion = new Constellation(1L, ORION);
		DeepSkyObject m42 = new DeepSkyObject(1L, M42, orion);

		when(deepSkyObjectService.findById(1L)).thenReturn(m42);

		HtmlPage page = webClient.getPage("/deepskyobject/modify/1");
		final HtmlForm deepSkyObjectForm = page.getFormByName("deepSkyObjectForm");

		assertThat(deepSkyObjectForm.getInputByValue(M42).getValueAttribute()).isEqualToIgnoringCase(M42);
	}

	@Test
	public void testModifyConstellationWhenExists() throws Exception {
		Constellation orion = new Constellation(1L, ORION);
		DeepSkyObject m42 = new DeepSkyObject(1L, M42, orion);

		when(constellationService.findAll()).thenReturn(Arrays.asList(
				orion
		));
		when(deepSkyObjectService.findById(1L)).thenReturn(m42);

		HtmlPage page = webClient.getPage("/deepskyobject/modify/1");
		final HtmlForm deepSkyObjectForm = page.getFormByName("deepSkyObjectForm");

		deepSkyObjectForm.getInputByValue(M42).setValueAttribute(M42 + " changed");
		deepSkyObjectForm.getSelectByName("constellation").setSelectedAttribute("1", true);
		deepSkyObjectForm.getButtonByName("submitButton").click();

		InOrder inOrder = inOrder(constellationService, deepSkyObjectService);
		inOrder.verify(deepSkyObjectService).findAll();
		inOrder.verify(constellationService).findAll();
		inOrder.verify(deepSkyObjectService).updateById(eq(1L), any(DeepSkyObject.class));
		inOrder.verify(deepSkyObjectService).findAll();
		inOrder.verify(constellationService).findAll();
	}

	@Test
	public void testDeleteConstellation() throws Exception {
		webClient.getPage("/deepskyobject/delete/1");

		verify(deepSkyObjectService).findAll();
		verify(deepSkyObjectService).deleteById(1L);
	}

}
