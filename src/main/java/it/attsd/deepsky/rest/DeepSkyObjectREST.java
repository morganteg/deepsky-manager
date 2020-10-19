package it.attsd.deepsky.rest;

import java.util.ArrayList;
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

import it.attsd.deepsky.dto.DeepSkyObjectRequest;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.service.DeepSkyObjectService;

@RestController()
@RequestMapping("/api/deepskyobject")
public class DeepSkyObjectREST {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectREST.class);

	@Autowired
	private DeepSkyObjectService deepSkyObjectService;

	@GetMapping(value = "", produces = "application/json")
	public @ResponseBody List<DeepSkyObject> getAll() {
		List<DeepSkyObject> deepSkyObjects = new ArrayList<DeepSkyObject>();

		try {
			deepSkyObjects = deepSkyObjectService.findAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return deepSkyObjects;
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public @ResponseBody DeepSkyObject getById(@PathVariable long id) {
		logger.debug("{}", id);

		DeepSkyObject deepSkyObject = new DeepSkyObject();

		try {
			deepSkyObject = deepSkyObjectService.findById(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return deepSkyObject;
	}

	@PostMapping(value = "", produces = "application/json", consumes = "application/json")
	public @ResponseBody DeepSkyObject save(@RequestBody DeepSkyObjectRequest deepSkyObjectRequest) throws Exception {
		logger.debug("{}", deepSkyObjectRequest);

		DeepSkyObject deepSkyObjectSaved = null;

		try {
			deepSkyObjectSaved = deepSkyObjectService.save(deepSkyObjectRequest.getConstellationId(),
					deepSkyObjectRequest.getDeepSkyObjectTypeId(), deepSkyObjectRequest.getName());
		} catch (Exception e) {
			logger.error("{}", e);
			throw e;
		}

		return deepSkyObjectSaved;
	}

	@PutMapping(value = "", produces = "application/json", consumes = "application/json")
	public void update(@RequestBody DeepSkyObject deepSkyObject) {
		logger.debug("{}", deepSkyObject);

		try {
			deepSkyObjectService.update(deepSkyObject);
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	public void delete(@PathVariable long id) {
		logger.debug("{}", id);

		try {
			deepSkyObjectService.delete(id);
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}

}
