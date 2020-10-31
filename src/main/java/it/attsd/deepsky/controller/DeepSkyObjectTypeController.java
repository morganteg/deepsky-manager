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

import it.attsd.deepsky.controller.form.DeepSkyObjectTypeForm;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@Controller
public class DeepSkyObjectTypeController {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectTypeController.class);
	
	private static final String attributeForm = "deepSkyObjectTypeForm";
	private static final String attributeDeepSkyObjectTypes = "deepSkyObjectTypes";
	private static final String targetDeepSkyObjectType = "deepSkyObjectType/deepSkyObjectType";
	
	@Autowired
	DeepSkyObjectTypeService deepSkyObjectTypeService;

	@GetMapping(value = "/deepskyobjecttype")
	public String getDeepSkyObjectTypes(Model model) {
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();

		model.addAttribute(attributeForm, new DeepSkyObjectTypeForm());
		model.addAttribute(attributeDeepSkyObjectTypes, deepSkyObjectTypes);

		return targetDeepSkyObjectType;
	}

	@PostMapping("/deepskyobjecttype")
	public String saveDeepSkyObjectType(@ModelAttribute DeepSkyObjectTypeForm deepSkyObjectTypeForm, Model model) {
		try {
			if(StringUtils.isNotEmpty(deepSkyObjectTypeForm.getType())) {
				if(deepSkyObjectTypeForm.getId() == 0) {
					// Save
					deepSkyObjectTypeService.save(new DeepSkyObjectType(deepSkyObjectTypeForm.getType().toLowerCase()));
				}else {
					// Update
					deepSkyObjectTypeService.update(new DeepSkyObjectType(deepSkyObjectTypeForm.getId(), deepSkyObjectTypeForm.getType().toLowerCase()));
				}
			}
		} catch (DeepSkyObjectTypeAlreadyExistsException e) {
			logger.info(e.getMessage());
			model.addAttribute("error", e.getMessage());
		}
		
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		
		model.addAttribute(attributeForm, new DeepSkyObjectTypeForm());
		model.addAttribute(attributeDeepSkyObjectTypes, deepSkyObjectTypes);
		
		return targetDeepSkyObjectType;
	}
	
	@GetMapping(value = "/deepskyobjecttype/modify/{id}")
	public String modifyDeepSkyObjectType(@PathVariable long id, Model model) {
		DeepSkyObjectType deepSkyObjectType = deepSkyObjectTypeService.findById(id);
		
		DeepSkyObjectTypeForm deepSkyObjectTypeForm = new DeepSkyObjectTypeForm();
		deepSkyObjectTypeForm.setId(deepSkyObjectType.getId());
		deepSkyObjectTypeForm.setType(deepSkyObjectType.getType());
		model.addAttribute(attributeForm, deepSkyObjectTypeForm);
		
		List<DeepSkyObjectType> deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		model.addAttribute(attributeDeepSkyObjectTypes, deepSkyObjectTypes);

		return targetDeepSkyObjectType;
	}
	
	@GetMapping(value = "/deepskyobjecttype/delete/{id}")
	public String deleteDeepSkyObjectType(@PathVariable long id, Model model) {
		deepSkyObjectTypeService.delete(id);

		return "redirect:/deepskyobjecttype";
	}

}
