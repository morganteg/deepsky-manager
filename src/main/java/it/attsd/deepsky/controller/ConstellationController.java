package it.attsd.deepsky.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.attsd.deepsky.controller.form.ConstellationForm;
import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.service.ConstellationService;

@Controller
public class ConstellationController {
	@Autowired
	ConstellationService constellationService;

	@GetMapping(value = "/constellation")
	public String constellations(Model model) {
		List<Constellation> constellations = constellationService.findAll();

		model.addAttribute("constellation", new ConstellationForm());
		model.addAttribute("constellations", constellations);

		return "constellation/constellation";
	}

	@PostMapping("/constellation")
	public String constellations(@ModelAttribute ConstellationForm constellationForm, Model model) {
		try {
			if(constellationForm != null && StringUtils.isNotEmpty(constellationForm.getName())) {
				constellationService.save(new Constellation(constellationForm.getName().toLowerCase()));
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("constellation", new ConstellationForm());
		
		List<Constellation> constellations = constellationService.findAll();
		model.addAttribute("constellations", constellations);
		
		return "constellation/constellation";
	}

}
