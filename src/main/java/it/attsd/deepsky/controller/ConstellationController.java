package it.attsd.deepsky.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.attsd.deepsky.controller.form.ConstellationForm;
import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.service.ConstellationService;

@Controller
public class ConstellationController {
	private Logger logger = LoggerFactory.getLogger(ConstellationController.class);
	
	private static final String ATTRIBUTE_FORM = "constellationForm";
	private static final String ATTRIBUTE_CONSTELLATIONS = "constellations";
	private static final String TARGET_CONSTELLATION = "constellation/constellation";
	
	@Autowired
	ConstellationService constellationService;

	@GetMapping(value = "/constellation")
	public String getConstellations(Model model) {
		List<Constellation> constellations = constellationService.findAll();

		model.addAttribute(ATTRIBUTE_FORM, new ConstellationForm());
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);

		return TARGET_CONSTELLATION;
	}

	@PostMapping("/constellation")
	public String saveConstellations(@ModelAttribute ConstellationForm constellationForm, Model model) {
		try {
			if(constellationForm != null && StringUtils.isNotEmpty(constellationForm.getName())) {
				if(constellationForm.getId() == 0) {
					// Save
					constellationService.save(new Constellation(constellationForm.getName().toLowerCase()));
				}else {
					// Update
					constellationService.update(new Constellation(constellationForm.getId(), constellationForm.getName().toLowerCase()));
				}
			}
		} catch (ConstellationAlreadyExistsException e) {
			logger.info(e.getMessage());
			model.addAttribute("error", e.getMessage());
		}
		
		List<Constellation> constellations = constellationService.findAll();

		model.addAttribute(ATTRIBUTE_FORM, new ConstellationForm());
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);

		return TARGET_CONSTELLATION;
	}
	
	@GetMapping(value = "/constellation/modify/{id}")
	public String modifyConstellation(@PathVariable long id, Model model) {
		Constellation constellation = constellationService.findById(id);
		
		ConstellationForm constellationForm = new ConstellationForm();
		constellationForm.setId(constellation.getId());
		constellationForm.setName(constellation.getName());
		model.addAttribute(ATTRIBUTE_FORM, constellationForm);
		
		List<Constellation> constellations = constellationService.findAll();
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);

		return TARGET_CONSTELLATION;
	}
	
	@GetMapping(value = "/constellation/delete/{id}")
	public String deleteConstellation(@PathVariable long id, Model model) {
		constellationService.delete(id);

		return "redirect:/constellation";
	}

}
