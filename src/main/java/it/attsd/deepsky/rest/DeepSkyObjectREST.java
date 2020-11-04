package it.attsd.deepsky.rest;

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

import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.pojo.deepskyobject.DeepSkyObjectPojo;
import it.attsd.deepsky.service.DeepSkyObjectService;

@RestController()
@RequestMapping("/api/deepskyobject")
public class DeepSkyObjectREST {
	@Autowired
	private DeepSkyObjectService deepSkyObjectService;

	@GetMapping(value = "", produces = "application/json")
	public @ResponseBody List<DeepSkyObject> getAll() {
		return deepSkyObjectService.findAll();
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public @ResponseBody DeepSkyObject getById(@PathVariable long id) {
		return deepSkyObjectService.findById(id);
	}

	@PostMapping(value = "", produces = "application/json", consumes = "application/json")
	public @ResponseBody DeepSkyObject save(@RequestBody DeepSkyObjectPojo deepSkyObjectSaveRequest)
			throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException,
			DeepSkyObjectAlreadyExistsException {
		return deepSkyObjectService.save(deepSkyObjectSaveRequest.getConstellationId(),
				deepSkyObjectSaveRequest.getDeepSkyObjectTypeId(), deepSkyObjectSaveRequest.getName());
	}

	@PutMapping(value = "", produces = "application/json", consumes = "application/json")
	public @ResponseBody DeepSkyObject update(@RequestBody DeepSkyObjectPojo deepSkyObjectUpdateRequest)
			throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, DeepSkyObjectNotFoundException {
		return deepSkyObjectService.update(deepSkyObjectUpdateRequest.getId(), deepSkyObjectUpdateRequest.getName(),
				deepSkyObjectUpdateRequest.getConstellationId(), deepSkyObjectUpdateRequest.getDeepSkyObjectTypeId());
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	public void delete(@PathVariable long id) {
		deepSkyObjectService.delete(id);
	}

}
