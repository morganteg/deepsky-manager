package it.attsd.deepsky.unit.controller;

import java.util.List;

import it.attsd.deepsky.dto.ConstellationDto;
import it.attsd.deepsky.exceptions.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exceptions.ConstellationIsStillUsedException;
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

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.unit.service.ConstellationService;

@RestController()
@RequestMapping("/api/constellation")
public class ConstellationRestController {

	@Autowired
	private ConstellationService constellationService;

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Constellation> getAll() {
		return constellationService.findAll();
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Constellation getById(@PathVariable long id) {
		return constellationService.findById(id);
	}

	@GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Constellation getByName(@PathVariable String name) {
		return constellationService.findByName(name);
	}

	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Constellation save(@RequestBody ConstellationDto constellationDto) throws ConstellationAlreadyExistsException {
		Constellation constellation = dtoToConstellation(constellationDto);

		return constellationService.save(constellation);
	}

	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Constellation update(@PathVariable long id, @RequestBody ConstellationDto constellationDto) {
		Constellation constellation = dtoToConstellation(constellationDto);

		return constellationService.updateById(id, constellation);
	}

	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void delete(@PathVariable long id) throws ConstellationIsStillUsedException {
		constellationService.deleteById(id);
	}

	private Constellation dtoToConstellation(ConstellationDto constellationDto) {
		return new Constellation(
				constellationDto.getId(),
				constellationDto.getName()
		);
	}

}
