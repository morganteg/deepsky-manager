package it.attsd.deepsky.rest;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.attsd.deepsky.dto.ConstellationRequest;
import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.service.ConstellationService;

@RestController()
@RequestMapping("/api/constellation")
public class ConstellationREST {
	private Logger logger = LoggerFactory.getLogger(ConstellationREST.class);

	@Autowired
	private ConstellationService constellationService;
	
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Constellation> getAll() {
		List<Constellation> constellations = new ArrayList<Constellation>();

		try {
			constellations = constellationService.findAll();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return constellations;
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Constellation getById(@PathVariable long id) {
		logger.debug("{}", id);

		Constellation constellation = new Constellation();

		try {
			constellation = constellationService.findById(id);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return constellation;
	}

	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Constellation save(@RequestBody ConstellationRequest constellationInput) {
		logger.debug("{}", constellationInput);

		Constellation constellationSaved = null;
		Constellation constellation = new Constellation(constellationInput.getName());

		try {
			constellationSaved = constellationService.save(constellation);
		} catch (Exception e) {
			logger.error("{}", e);
//			throw e;
		}

		return constellationSaved;
	}
	
	@PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void update(@RequestBody Constellation constellation) {
		logger.debug("{}", constellation);

		try {
			constellationService.update(constellation);
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}
	
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(@PathVariable long id) {
		logger.debug("{}", id);

		try {
			constellationService.delete(id);
		} catch (Exception e) {
			logger.error("{}", e);
		}
	}

}