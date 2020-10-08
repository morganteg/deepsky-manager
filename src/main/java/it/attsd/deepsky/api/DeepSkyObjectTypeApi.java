package it.attsd.deepsky.api;

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

import it.attsd.deepsky.dto.DeepSkyObjectTypeRequest;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.service.DeepSkyObjectTypeService;

@RestController()
@RequestMapping("/deepskyobjecttype")
public class DeepSkyObjectTypeApi {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectTypeApi.class);

	@Autowired
	private DeepSkyObjectTypeService deepSkyObjectTypeService;
	
	@GetMapping(value = "", produces = "application/json")
	public @ResponseBody List<DeepSkyObjectType> getAll() {
		List<DeepSkyObjectType> deepSkyObjectTypes = new ArrayList<DeepSkyObjectType>();

		try {
			deepSkyObjectTypes = deepSkyObjectTypeService.findAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return deepSkyObjectTypes;
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public @ResponseBody DeepSkyObjectType getById(@PathVariable long id) {
		logger.debug("{}", id);

		DeepSkyObjectType deepSkyObjectType = new DeepSkyObjectType();

		try {
			deepSkyObjectType = deepSkyObjectTypeService.findById(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return deepSkyObjectType;
	}

	@PostMapping(value = "", produces = "application/json", consumes = "application/json")
	public @ResponseBody DeepSkyObjectType save(@RequestBody DeepSkyObjectTypeRequest deepSkyObjectTypeRequest) throws Exception {
		logger.debug("{}", deepSkyObjectTypeRequest);

		DeepSkyObjectType deepSkyObjectTypeSaved = null;
		DeepSkyObjectType deepSkyObjectType = new DeepSkyObjectType(deepSkyObjectTypeRequest.getType());

		try {
			deepSkyObjectTypeSaved = deepSkyObjectTypeService.save(deepSkyObjectType);
		} catch (Exception e) {
			logger.error("{}", e);
			throw e;
		}

		return deepSkyObjectTypeSaved;
	}
	
	@PutMapping(value = "", produces = "application/json", consumes = "application/json")
	public void update(@RequestBody DeepSkyObjectType deepSkyObjectType) {
		logger.debug("{}", deepSkyObjectType);

		try {
			deepSkyObjectTypeService.update(deepSkyObjectType);
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}
	
	@DeleteMapping(value = "/{id}", produces = "application/json")
	public void delete(@PathVariable long id) {
		logger.debug("{}", id);

		try {
			deepSkyObjectTypeService.delete(id);
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}

}
