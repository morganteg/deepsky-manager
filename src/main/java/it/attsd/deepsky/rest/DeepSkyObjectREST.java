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

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.pojo.deepskyobject.DeepSkyObjectSaveRequest;
import it.attsd.deepsky.pojo.deepskyobject.DeepSkyObjectUpdateRequest;
import it.attsd.deepsky.service.ConstellationService;
import it.attsd.deepsky.service.DeepSkyObjectService;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RestController()
@RequestMapping("/api/deepskyobject")
public class DeepSkyObjectREST {
	@Autowired
	private ConstellationService constellationService;

	@Autowired
	private DeepSkyObjectTypeService deepSkyObjectTypeService;

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
	public @ResponseBody DeepSkyObject save(@RequestBody DeepSkyObjectSaveRequest deepSkyObjectSaveRequest)
			throws Exception {
		return deepSkyObjectService.save(deepSkyObjectSaveRequest.getConstellationId(),
				deepSkyObjectSaveRequest.getDeepSkyObjectTypeId(), deepSkyObjectSaveRequest.getName());
	}

	@PutMapping(value = "", produces = "application/json", consumes = "application/json")
	public @ResponseBody DeepSkyObject update(@RequestBody DeepSkyObjectUpdateRequest deepSkyObjectUpdateRequest) {
		DeepSkyObject deepSkyObjectFound = deepSkyObjectService.findById(deepSkyObjectUpdateRequest.getId());

		deepSkyObjectFound.setName(deepSkyObjectUpdateRequest.getName());

		if (deepSkyObjectUpdateRequest.getConstellationId() != deepSkyObjectFound.getConstellation().getId()) {
			Constellation constellation = constellationService
					.findById(deepSkyObjectUpdateRequest.getConstellationId());
			deepSkyObjectFound.setConstellation(constellation);
		}

		if (deepSkyObjectUpdateRequest.getDeepSkyObjectTypeId() != deepSkyObjectFound.getType().getId()) {
			DeepSkyObjectType deepSkyObjectType = deepSkyObjectTypeService
					.findById(deepSkyObjectUpdateRequest.getDeepSkyObjectTypeId());
			deepSkyObjectFound.setType(deepSkyObjectType);
		}

		return deepSkyObjectService.update(deepSkyObjectFound);
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	public void delete(@PathVariable long id) {
		deepSkyObjectService.delete(id);
	}

}
