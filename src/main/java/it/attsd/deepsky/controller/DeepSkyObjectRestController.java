package it.attsd.deepsky.controller;

import java.util.List;

import it.attsd.deepsky.dto.ConstellationDto;
import it.attsd.deepsky.dto.DeepSkyObjectDto;
import it.attsd.deepsky.exceptions.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.service.DeepSkyObjectService;
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

import it.attsd.deepsky.model.DeepSkyObject;

@RestController()
@RequestMapping("/api/deepskyobject")
public class DeepSkyObjectRestController {
	@Autowired
	private DeepSkyObjectService deepSkyObjectService;

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<DeepSkyObject> getAll() {
		return deepSkyObjectService.findAll();
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DeepSkyObject getById(@PathVariable long id) {
		return deepSkyObjectService.findById(id);
	}

	@GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DeepSkyObject getByName(@PathVariable String name) {
		return deepSkyObjectService.findByName(name);
	}

	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DeepSkyObject save(@RequestBody DeepSkyObjectDto deepSkyObjectDto) throws DeepSkyObjectAlreadyExistsException {
		DeepSkyObject deepSkyObject = dtoToDeepSkyObject(deepSkyObjectDto);

		return deepSkyObjectService.save(deepSkyObject);
	}

	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody DeepSkyObject update(@PathVariable long id, @RequestBody DeepSkyObjectDto deepSkyObjectDto) {
		DeepSkyObject deepSkyObject = dtoToDeepSkyObject(deepSkyObjectDto);

		return deepSkyObjectService.updateById(id, deepSkyObject);
	}

	@DeleteMapping(value = "/{id}", produces = "application/json")
	public void delete(@PathVariable long id) {
		deepSkyObjectService.deleteById(id);
	}

	private Constellation dtoToConstellation(ConstellationDto constellationDto) {
		return new Constellation(
				constellationDto.getId(),
				constellationDto.getName()
		);
	}

	private DeepSkyObject dtoToDeepSkyObject(DeepSkyObjectDto deepSkyObjectDto) {
		return new DeepSkyObject(
				deepSkyObjectDto.getId(),
				deepSkyObjectDto.getName(),
				dtoToConstellation(deepSkyObjectDto.getConstellation())
		);
	}

}
