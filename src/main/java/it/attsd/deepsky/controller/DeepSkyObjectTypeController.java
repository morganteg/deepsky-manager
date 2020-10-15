package it.attsd.deepsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.attsd.deepsky.controller.form.DeepSkyObjectTypeForm;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@Controller
public class DeepSkyObjectTypeController {
	@Autowired
	DeepSkyObjectTypeService deepSkyObjectTypeService;

	@GetMapping(value = "/deepskyobjecttype")
	public String constellations(Model model) {
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();

		model.addAttribute("deepSkyObjectType", new DeepSkyObjectTypeForm());
		model.addAttribute("deepSkyObjectTypes", deepSkyObjectTypes);

		return "deepSkyObjectType/deepSkyObjectType";
	}

	@PostMapping("/deepskyobjecttype")
	public String constellations(@ModelAttribute DeepSkyObjectTypeForm deepSkyObjectTypeForm, Model model) {
		try {
			deepSkyObjectTypeService.save(new DeepSkyObjectType(deepSkyObjectTypeForm.getType().toLowerCase()));
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		
		model.addAttribute("deepSkyObjectType", new DeepSkyObjectTypeForm());
		model.addAttribute("deepSkyObjectTypes", deepSkyObjectTypes);
		
		return "deepSkyObjectType/deepSkyObjectType";
	}

}
