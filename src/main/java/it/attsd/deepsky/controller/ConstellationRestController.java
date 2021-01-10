package it.attsd.deepsky.controller;

import it.attsd.deepsky.dto.ConstellationDto;
import it.attsd.deepsky.exceptions.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exceptions.ConstellationIsStillUsedException;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.service.ConstellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	@ExceptionHandler(ConstellationAlreadyExistsException.class)
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
