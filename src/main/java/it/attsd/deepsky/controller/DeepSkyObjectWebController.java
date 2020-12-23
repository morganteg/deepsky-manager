package it.attsd.deepsky.controller;

import it.attsd.deepsky.exceptions.DeepSkyObjectAlreadyExistsException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;

@Controller
public class DeepSkyObjectWebController {
	private static final String ATTRIBUTE_DEEPSKYOBJECT = "deepSkyObject";
	private static final String ATTRIBUTE_DEEPSKYOBJECTS = "deepSkyObjects";
	private static final String ATTRIBUTE_CONSTELLATIONS = "constellations";
	private static final String TARGET_DEEPSKYOBJECT = "deepSkyObject/deepSkyObject";
	private static final String ATTRIBUTE_ERROR = "error";

	@Autowired
	ConstellationService constellationService;

	@Autowired
	DeepSkyObjectService deepSkyObjectService;

	@GetMapping(value = "/deepskyobject")
	public String getDeepSkyObjects(Model model) {
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECT, new DeepSkyObject());
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjectService.findAll());
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellationService.findAll());

		return TARGET_DEEPSKYOBJECT;
	}

	@PostMapping("/deepskyobject")
	public String saveDeepSkyObject(@ModelAttribute("deepskyobject") DeepSkyObject deepSkyObject, Model model)
			throws Exception {
		if (StringUtils.isEmpty(deepSkyObject.getName()) || deepSkyObject.getConstellation() == null) {
			model.addAttribute(ATTRIBUTE_ERROR, "Please, fill all mandatory attributes");
			model.addAttribute(ATTRIBUTE_DEEPSKYOBJECT, deepSkyObject);
			model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjectService.findAll());
			model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellationService.findAll());
			
			return TARGET_DEEPSKYOBJECT;
		} else {
			final Long id = deepSkyObject.getId();
			if (id == null) {
				try {
					deepSkyObjectService.save(deepSkyObject);
				}catch(DeepSkyObjectAlreadyExistsException e){
					model.addAttribute(ATTRIBUTE_ERROR, "A DeepSkyObject with the same name already exists");
					model.addAttribute(ATTRIBUTE_DEEPSKYOBJECT, deepSkyObject);
					model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjectService.findAll());
					model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellationService.findAll());

					return TARGET_DEEPSKYOBJECT;
				}
			} else {
				deepSkyObjectService.updateById(id, deepSkyObject);
			}
		}

		return "redirect:/deepskyobject";
	}

	@GetMapping(value = "/deepskyobject/modify/{id}")
	public String modifyDeepSkyObject(@PathVariable long id, Model model) {
		DeepSkyObject deepSkyObject = deepSkyObjectService.findById(id);
		if (deepSkyObject == null) {
			deepSkyObject = new DeepSkyObject();
			model.addAttribute(ATTRIBUTE_ERROR, "Deep-Sky object not found");
		}

		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECT, deepSkyObject);
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjectService.findAll());
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellationService.findAll());

		return TARGET_DEEPSKYOBJECT;
	}

	@GetMapping(value = "/deepskyobject/delete/{id}")
	public String deleteDeepSkyObject(@PathVariable long id, Model model) {
		deepSkyObjectService.deleteById(id);

		return "redirect:/deepskyobject";
	}

}
