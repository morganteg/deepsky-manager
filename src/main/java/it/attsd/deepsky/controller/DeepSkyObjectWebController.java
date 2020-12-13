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
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;

@Controller
public class DeepSkyObjectWebController {
	private static final String ATTRIBUTE_DEEPSKYOBJECT = "deepSkyObject";
	private static final String ATTRIBUTE_DEEPSKYOBJECTS = "deepSkyObjects";
	private static final String ATTRIBUTE_CONSTELLATIONS = "constellations";
	private static final String TARGET_DEEPSKYOBJECT = "deepSkyObject/deepSkyObject";
	private static final String ATTRIBUTE_MESSAGE = "message";

	@Autowired
	ConstellationService constellationService;

	@Autowired
	DeepSkyObjectService deepSkyObjectService;

	@GetMapping(value = "/deepskyobject")
	public String getDeepSkyObjects(Model model) {
		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECT, new DeepSkyObject());
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjects);

		List<Constellation> constellations = constellationService.findAll();
		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);

		return TARGET_DEEPSKYOBJECT;
	}
	
	@PostMapping("/deepskyobject")
	public String saveDeepSkyObject(@ModelAttribute("deepskyobject") DeepSkyObject deepSkyObject) {
		final Long id = deepSkyObject.getId();
		if (id == null) {
			deepSkyObjectService.save(deepSkyObject);
		} else {
			deepSkyObjectService.updateById(id, deepSkyObject);
		}

		return "redirect:/deepskyobject";
	}

//	@PostMapping("/deepskyobject")
//	public String saveDeepSkyObject(@ModelAttribute DeepSkyObjectPojo deepSkyObjectPojo, Model model) {
//		try {
//			if (StringUtils.isNotEmpty(deepSkyObjectPojo.getName()) && deepSkyObjectPojo.getConstellationId() > 0
//					&& deepSkyObjectPojo.getDeepSkyObjectTypeId() > 0) {
//				if (deepSkyObjectPojo.getId() == 0) {
//					// Save
//					deepSkyObjectService.save(deepSkyObjectPojo.getConstellationId(),
//							deepSkyObjectPojo.getDeepSkyObjectTypeId(), deepSkyObjectPojo.getName().toLowerCase());
//				} else {
//					// Update
//					deepSkyObjectService.update(deepSkyObjectPojo.getId(), deepSkyObjectPojo.getName().toLowerCase(),
//							deepSkyObjectPojo.getConstellationId(), deepSkyObjectPojo.getDeepSkyObjectTypeId());
//				}
//			}
//		} catch (DeepSkyObjectAlreadyExistsException | ConstellationNotFoundException
//				| DeepSkyObjectTypeNotFoundException | DeepSkyObjectNotFoundException e) {
//			logger.info(e.getMessage());
//			model.addAttribute("error", e.getMessage());
//		}
//
//		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
//		model.addAttribute(ATTRIBUTE_FORM, new DeepSkyObjectPojo());
//		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjects);
//
//		List<Constellation> constellations = constellationService.findAll();
//		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
//		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);
//		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);
//
//		return TARGET_DEEPSKYOBJECT;
//	}

	@GetMapping(value = "/deepskyobject/modify/{id}")
	public String modifyDeepSkyObject(@PathVariable long id, Model model) {
		DeepSkyObject deepSkyObject = deepSkyObjectService.findById(id);
		if(deepSkyObject == null) {
			deepSkyObject = new DeepSkyObject();
			model.addAttribute(ATTRIBUTE_MESSAGE, "DeepSkyObject not found");
		}
		
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECT, deepSkyObject);
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjectService.findAll());

		return TARGET_DEEPSKYOBJECT;
		
		
		
//		DeepSkyObject deepSkyObject = deepSkyObjectService.findById(id);
//
//		DeepSkyObjectPojo deepSkyObjectPojo = new DeepSkyObjectPojo();
//		deepSkyObjectPojo.setId(deepSkyObject.getId());
//		deepSkyObjectPojo.setName(deepSkyObject.getName());
//		deepSkyObjectPojo.setConstellationId(deepSkyObject.getConstellation().getId());
//		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECT, deepSkyObjectPojo);
//
//		List<DeepSkyObject> deepSkyObjects = deepSkyObjectService.findAll();
//		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTS, deepSkyObjects);
//
//		List<Constellation> constellations = constellationService.findAll();
//		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
//		model.addAttribute(ATTRIBUTE_CONSTELLATIONS, constellations);
//		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);
//
//		return TARGET_DEEPSKYOBJECT;
	}

	@GetMapping(value = "/deepskyobject/delete/{id}")
	public String deleteDeepSkyObject(@PathVariable long id, Model model) {
		deepSkyObjectService.deleteById(id);

		return "redirect:/deepskyobject";
	}

}
