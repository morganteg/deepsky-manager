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

import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.pojo.DeepSkyObjectTypePojo;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@Controller
public class DeepSkyObjectTypeController {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectTypeController.class);
	
	private static final String ATTRIBUTE_FORM = "deepSkyObjectTypeForm";
	private static final String ATTRIBUTE_DEEPSKYOBJECTTYPES = "deepSkyObjectTypes";
	private static final String TARGET_DEEPSKYOBJECTTYPE = "deepSkyObjectType/deepSkyObjectType";
	
	@Autowired
	DeepSkyObjectTypeService deepSkyObjectTypeService;

	@GetMapping(value = "/deepskyobjecttype")
	public String getDeepSkyObjectTypes(Model model) {
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();

		model.addAttribute(ATTRIBUTE_FORM, new DeepSkyObjectTypePojo());
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);

		return TARGET_DEEPSKYOBJECTTYPE;
	}

	@PostMapping("/deepskyobjecttype")
	public String saveDeepSkyObjectType(@ModelAttribute DeepSkyObjectTypePojo deepSkyObjectTypePojo, Model model) {
		try {
			if(StringUtils.isNotEmpty(deepSkyObjectTypePojo.getType())) {
				if(deepSkyObjectTypePojo.getId() == 0) {
					// Save
					deepSkyObjectTypeService.save(new DeepSkyObjectType(deepSkyObjectTypePojo.getType().toLowerCase()));
				}else {
					// Update
					deepSkyObjectTypeService.update(new DeepSkyObjectType(deepSkyObjectTypePojo.getId(), deepSkyObjectTypePojo.getType().toLowerCase()));
				}
			}
		} catch (DeepSkyObjectTypeAlreadyExistsException e) {
			logger.info(e.getMessage());
			model.addAttribute("error", e.getMessage());
		}
		
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		
		model.addAttribute(ATTRIBUTE_FORM, new DeepSkyObjectTypePojo());
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);
		
		return TARGET_DEEPSKYOBJECTTYPE;
	}
	
	@GetMapping(value = "/deepskyobjecttype/modify/{id}")
	public String modifyDeepSkyObjectType(@PathVariable long id, Model model) throws DeepSkyObjectTypeNotFoundException {
		DeepSkyObjectType deepSkyObjectType = deepSkyObjectTypeService.findById(id);
		
		DeepSkyObjectTypePojo deepSkyObjectTypePojo = new DeepSkyObjectTypePojo();
		deepSkyObjectTypePojo.setId(deepSkyObjectType.getId());
		deepSkyObjectTypePojo.setType(deepSkyObjectType.getType());
		model.addAttribute(ATTRIBUTE_FORM, deepSkyObjectTypePojo);
		
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		model.addAttribute(ATTRIBUTE_DEEPSKYOBJECTTYPES, deepSkyObjectTypes);

		return TARGET_DEEPSKYOBJECTTYPE;
	}
	
	@GetMapping(value = "/deepskyobjecttype/delete/{id}")
	public String deleteDeepSkyObjectType(@PathVariable long id, Model model) {
		deepSkyObjectTypeService.delete(id);

		return "redirect:/deepskyobjecttype";
	}

}
