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

import it.attsd.deepsky.controller.form.DeepSkyObjectForm;
import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@Controller
public class DeepSkyObjectController {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectController.class);

	private final String ATTRIBUTE_FORM = "deepSkyObjectForm";
	private final String ATTRIBUTE_DEEPSKYOBJECTS = "deepSkyObjects";
	private final String ATTRIBUTE_CONSTELLATIONS = "constellations";
	private final String ATTRIBUTE_DEEPSKYOBJECTTYPES = "deepSkyObjectTypes";

	@Autowired
	ConstellationService constellationService;

	@Autowired
	DeepSkyObjectTypeService deepSkyObjectTypeService;

	@Autowired
	DeepSkyObjectService deepSkyObjectService;

	@GetMapping(value = "/deepskyobject")
	public String getDeepSkyObjects(Model model) {
		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
		model.addAttribute(ATTRIBUTE_FORM, new DeepSkyObjectForm());
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjects);

		List<Constellation> constellations = constellationService.findAll();
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);

		return "deepSkyObject/deepSkyObject";
	}

	@PostMapping("/deepskyobject")
	public String saveDeepSkyObject(@ModelAttribute DeepSkyObjectForm deepSkyObjectForm, Model model) {
		try {
			if (StringUtils.isNotEmpty(deepSkyObjectForm.getName()) && deepSkyObjectForm.getConstellationId() > 0
					&& deepSkyObjectForm.getDeepSkyObjectTypeId() > 0) {
				if (deepSkyObjectForm.getId() == 0) {
					// Save
					deepSkyObjectService.save(deepSkyObjectForm.getConstellationId(),
							deepSkyObjectForm.getDeepSkyObjectTypeId(), deepSkyObjectForm.getName().toLowerCase());
				} else {
					// Update
					deepSkyObjectService.update(deepSkyObjectForm.getId(), deepSkyObjectForm.getName().toLowerCase(),
							deepSkyObjectForm.getConstellationId(), deepSkyObjectForm.getDeepSkyObjectTypeId());
				}
			}
		} catch (DeepSkyObjectAlreadyExistsException | ConstellationNotFoundException
				| DeepSkyObjectTypeNotFoundException | DeepSkyObjectNotFoundException e) {
			logger.info(e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
		model.addAttribute(ATTRIBUTE_FORM, new DeepSkyObjectForm());
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjects);

		List<Constellation> constellations = constellationService.findAll();
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);

		return "deepSkyObject/deepSkyObject";
	}

	@GetMapping(value = "/deepskyobject/modify/{id}")
	public String modifyDeepSkyObject(@PathVariable long id, Model model) {
		DeepSkyObject deepSkyObject = deepSkyObjectService.findById(id);

		DeepSkyObjectForm deepSkyObjectForm = new DeepSkyObjectForm();
		deepSkyObjectForm.setId(deepSkyObject.getId());
		deepSkyObjectForm.setName(deepSkyObject.getName());
		deepSkyObjectForm.setConstellationId(deepSkyObject.getConstellation().getId());
		deepSkyObjectForm.setDeepSkyObjectTypeId(deepSkyObject.getType().getId());
		model.addAttribute(ATTRIBUTE_FORM, deepSkyObjectForm);

		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjects);

		List<Constellation> constellations = constellationService.findAll();
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);

		return "deepSkyObject/deepSkyObject";
	}

	@GetMapping(value = "/deepskyobject/delete/{id}")
	public String deleteDeepSkyObject(@PathVariable long id, Model model) {
		deepSkyObjectService.delete(id);

		return "redirect:/deepskyobject";
	}

}
