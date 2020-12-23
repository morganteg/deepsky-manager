package it.attsd.deepsky.controller;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.service.ConstellationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ConstellationWebController {
	private static final String ATTRIBUTE_CONSTELLATION = "constellation";
	private static final String ATTRIBUTE_CONSTELLATIONS = "constellations";
	private static final String ATTRIBUTE_ERROR = "error";
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
	public String saveConstellation(@ModelAttribute("constellation") Constellation constellation, Model model) {
		if (StringUtils.isEmpty(constellation.getName())) {
			model.addAttribute(ATTRIBUTE_ERROR, "Please, fill all mandatory attributes");
			model.addAttribute(ATTRIBUTE_CONSTELLATION, constellation);
			model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellationService.findAll());
			
			return TARGET_CONSTELLATION;
		} else {
			final Long id = constellation.getId();
			if (id == null) {
				constellationService.save(constellation);
			} else {
				constellationService.updateById(id, constellation);
			}
		}

		return "redirect:/constellation";
	}

	@GetMapping(value = "/constellation/modify/{id}")
	public String modifyConstellation(@PathVariable long id, Model model) {
		Constellation constellation = constellationService.findById(id);
		if(constellation == null) {
			constellation = new Constellation();
			model.addAttribute(ATTRIBUTE_ERROR, "Constellation not found");
		}
		
		model.addAttribute(ATTRIBUTE_CONSTELLATION, constellation);
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellationService.findAll());

		return TARGET_CONSTELLATION;
	}

	@GetMapping(value = "/constellation/delete/{id}")
	public String deleteConstellation(@PathVariable long id, Model model) {
		constellationService.deleteById(id);

		return "redirect:/constellation";
	}

}
