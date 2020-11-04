package it.attsd.deepsky.rest;

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

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.pojo.constellation.ConstellationPojo;
import it.attsd.deepsky.service.ConstellationService;

@RestController()
@RequestMapping("/api/constellation")
public class ConstellationREST {
	private Logger logger = LoggerFactory.getLogger(ConstellationREST.class);

	@Autowired
	private ConstellationService constellationService;

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Constellation> getAll() {
		return constellationService.findAll();
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Constellation getById(@PathVariable long id) {
		logger.debug("{}", id);

		return constellationService.findById(id);
	}

	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Constellation save(@RequestBody ConstellationPojo constellationSaveRequest) throws ConstellationAlreadyExistsException {
		return constellationService.save(new Constellation(constellationSaveRequest.getName()));
	}

	@PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Constellation update(@RequestBody ConstellationPojo constellationUpdateRequest) {
		logger.debug("{}", constellationUpdateRequest);

		Constellation constellation = new Constellation(constellationUpdateRequest.getId(),
				constellationUpdateRequest.getName());
		return constellationService.update(constellation);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(@PathVariable long id) {
		logger.debug("{}", id);

		constellationService.delete(id);
	}

}
