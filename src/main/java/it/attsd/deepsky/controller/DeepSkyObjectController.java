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

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.pojo.DeepSkyObjectPojo;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@Controller
public class DeepSkyObjectController {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectController.class);

	private static final String ATTRIBUTE_FORM = "deepSkyObjectForm";
	private static final String ATTRIBUTE_DEEPSKYOBJECTS = "deepSkyObjects";
	private static final String ATTRIBUTE_CONSTELLATIONS = "constellations";
	private static final String ATTRIBUTE_DEEPSKYOBJECTTYPES = "deepSkyObjectTypes";
	private static final String TARGET_DEEPSKYOBJECT = "deepSkyObject/deepSkyObject";

	@Autowired
	ConstellationService constellationService;

	@Autowired
	DeepSkyObjectTypeService deepSkyObjectTypeService;

	@Autowired
	DeepSkyObjectService deepSkyObjectService;

	@GetMapping(value = "/deepskyobject")
	public String getDeepSkyObjects(Model model) {
		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
		model.addAttribute(ATTRIBUTE_FORM, new DeepSkyObjectPojo());
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjects);

		List<Constellation> constellations = constellationService.findAll();
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);

		return TARGET_DEEPSKYOBJECT;
	}

	@PostMapping("/deepskyobject")
	public String saveDeepSkyObject(@ModelAttribute DeepSkyObjectPojo deepSkyObjectPojo, Model model) {
		try {
			if (StringUtils.isNotEmpty(deepSkyObjectPojo.getName()) && deepSkyObjectPojo.getConstellationId() > 0
					&& deepSkyObjectPojo.getDeepSkyObjectTypeId() > 0) {
				if (deepSkyObjectPojo.getId() == 0) {
					// Save
					deepSkyObjectService.save(deepSkyObjectPojo.getConstellationId(),
							deepSkyObjectPojo.getDeepSkyObjectTypeId(), deepSkyObjectPojo.getName().toLowerCase());
				} else {
					// Update
					deepSkyObjectService.update(deepSkyObjectPojo.getId(), deepSkyObjectPojo.getName().toLowerCase(),
							deepSkyObjectPojo.getConstellationId(), deepSkyObjectPojo.getDeepSkyObjectTypeId());
				}
			}
		} catch (DeepSkyObjectAlreadyExistsException | ConstellationNotFoundException
				| DeepSkyObjectTypeNotFoundException | DeepSkyObjectNotFoundException e) {
			logger.info(e.getMessage());
			model.addAttribute("error", e.getMessage());
		}

		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
		model.addAttribute(ATTRIBUTE_FORM, new DeepSkyObjectPojo());
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjects);

		List<Constellation> constellations = constellationService.findAll();
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);

		return TARGET_DEEPSKYOBJECT;
	}

	@GetMapping(value = "/deepskyobject/modify/{id}")
	public String modifyDeepSkyObject(@PathVariable long id, Model model) throws DeepSkyObjectNotFoundException {
		DeepSkyObject deepSkyObject = deepSkyObjectService.findById(id);

		DeepSkyObjectPojo deepSkyObjectPojo = new DeepSkyObjectPojo();
		deepSkyObjectPojo.setId(deepSkyObject.getId());
		deepSkyObjectPojo.setName(deepSkyObject.getName());
		deepSkyObjectPojo.setConstellationId(deepSkyObject.getConstellation().getId());
		deepSkyObjectPojo.setDeepSkyObjectTypeId(deepSkyObject.getType().getId());
		model.addAttribute(ATTRIBUTE_FORM, deepSkyObjectPojo);

		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjects);

		List<Constellation> constellations = constellationService.findAll();
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);

		return TARGET_DEEPSKYOBJECT;
	}

	@GetMapping(value = "/deepskyobject/delete/{id}")
	public String deleteDeepSkyObject(@PathVariable long id, Model model) {
		deepSkyObjectService.delete(id);

		return "redirect:/deepskyobject";
	}

}
