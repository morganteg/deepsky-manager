package it.attsd.deepsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.service.ConstellationService;

@Controller
public class ConstellationWebController {
//	private Logger logger = LoggerFactory.getLogger(ConstellationWebController.class);

	private static final String ATTRIBUTE_CONSTELLATION = "constellation";
	private static final String ATTRIBUTE_CONSTELLATIONS = "constellations";
	private static final String ATTRIBUTE_MESSAGE = "message";
	private static final String TARGET_CONSTELLATION = "constellation/constellation";

	@Autowired
	ConstellationService constellationService;

	@GetMapping(value = "/constellation")
	public String getConstellations(Model model) {
		List<Constellation> constellations = constellationService.findAll();

		model.addAttribute(ATTRIBUTE_CONSTELLATION, new Constellation());
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);

		return TARGET_CONSTELLATION;
	}

	@PostMapping("/constellation")
	public String saveConstellation(@ModelAttribute("constellation") Constellation constellation) {
		final Long id = constellation.getId();
		if (id == null) {
			constellationService.save(constellation);
		} else {
			constellationService.updateById(id, constellation);
		}
		
//		try {
//			if (StringUtils.isNotEmpty(constellationPojo.getName())) {
//				if (constellationPojo.getId() == 0) {
//					// Save
//					constellationService.save(new Constellation(constellationPojo.getName().toLowerCase()));
//				} else {
//					// Update
//					constellationService.update(
//							new Constellation(constellationPojo.getId(), constellationPojo.getName().toLowerCase()));
//				}
//			}
//		} catch (ConstellationAlreadyExistsException e) {
//			logger.info(e.getMessage());
//			model.addAttribute("error", e.getMessage());
//		}
//
//		List<Constellation> constellations = constellationService.findAll();
//
//		model.addAttribute(ATTRIBUTE_FORM, new ConstellationPojo());
//		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);

		return "redirect:/constellation";
	}

	@GetMapping(value = "/constellation/modify/{id}")
	public String modifyConstellation(@PathVariable long id, Model model) {
		Constellation constellation = constellationService.findById(id);
		if(constellation == null) {
			constellation = new Constellation();
			model.addAttribute(ATTRIBUTE_MESSAGE, "Constellation not found");
		}
		
		model.addAttribute(ATTRIBUTE_CONSTELLATION, constellation);
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellationService.findAll());

		return TARGET_CONSTELLATION;
	}

//	@GetMapping(value = "/constellation/delete/{id}")
//	public String deleteConstellation(@PathVariable long id, Model model) {
//		constellationService.delete(id);
//
//		return "redirect:/constellation";
//	}

}
