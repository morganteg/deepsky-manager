package it.attsd.deepsky.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import it.attsd.deepsky.pojo.deepskyobject.DeepSkyObjectSaveRequest;
import it.attsd.deepsky.service.DeepSkyObjectService;

@RestController()
@RequestMapping("/api/deepskyobject")
public class DeepSkyObjectREST {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectREST.class);

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
	public @ResponseBody DeepSkyObject save(@RequestBody DeepSkyObjectSaveRequest deepSkyObjectRequest) throws Exception {
		return deepSkyObjectService.save(deepSkyObjectRequest.getConstellationId(),
				deepSkyObjectRequest.getDeepSkyObjectTypeId(), deepSkyObjectRequest.getName());
	}

	@PutMapping(value = "", produces = "application/json", consumes = "application/json")
	public @ResponseBody DeepSkyObject update(@RequestBody DeepSkyObject deepSkyObject) {
		return deepSkyObjectService.update(deepSkyObject);
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	public void delete(@PathVariable long id) {
		try {
			deepSkyObjectService.delete(id);
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}

}
