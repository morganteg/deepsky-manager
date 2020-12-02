package it.attsd.deepsky.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.pojo.DeepSkyObjectTypePojo;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RestController()
@RequestMapping("/api/deepskyobjecttype")
public class DeepSkyObjectTypeREST {

	@Autowired
	private DeepSkyObjectTypeService deepSkyObjectTypeService;

	@GetMapping(value = "", produces = "application/json")
	public @ResponseBody List<DeepSkyObjectType> getAll() {
		return deepSkyObjectTypeService.findAll();
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public @ResponseBody DeepSkyObjectType getById(@PathVariable long id) throws DeepSkyObjectTypeNotFoundException {
		return deepSkyObjectTypeService.findById(id);
	}

	@PostMapping(value = "", produces = "application/json", consumes = "application/json")
	public @ResponseBody DeepSkyObjectType save(@RequestBody DeepSkyObjectTypePojo deepSkyObjectTypeSaveRequest)
			throws DeepSkyObjectTypeAlreadyExistsException {
		DeepSkyObjectType deepSkyObjectType = new DeepSkyObjectType(deepSkyObjectTypeSaveRequest.getType());

		return deepSkyObjectTypeService.save(deepSkyObjectType);
	}

	@PutMapping(value = "", produces = "application/json", consumes = "application/json")
	public @ResponseBody DeepSkyObjectType update(@RequestBody DeepSkyObjectTypePojo deepSkyObjectTypeUpdateRequest) {
		DeepSkyObjectType deepSkyObjectType = new DeepSkyObjectType(deepSkyObjectTypeUpdateRequest.getId(),
				deepSkyObjectTypeUpdateRequest.getType());
		return deepSkyObjectTypeService.update(deepSkyObjectType);
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	public void delete(@PathVariable long id) {
		deepSkyObjectTypeService.delete(id);
	}

}