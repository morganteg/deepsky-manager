package it.attsd.deepsky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.model.ConstellationRepository;

@Service
public class ConstellationService {

	@Autowired
	private ConstellationRepository constellationRepository;

	public List<Constellation> findAll() {
		return constellationRepository.findAll();
	}

	public Constellation findById(long id) throws ConstellationNotFoundException {
		Constellation constellation = constellationRepository.findById(id);
		if (constellation == null) {
			throw new ConstellationNotFoundException();
		}

		return constellation;
	}

	public Constellation findByName(String name) {
		return constellationRepository.findByName(name);
	}

	public Constellation save(Constellation constellation) throws ConstellationAlreadyExistsException {
		return constellationRepository.save(constellation);
	}

	public Constellation update(Constellation constellation) {
		return constellationRepository.update(constellation);
	}

	public void delete(long id) {
		constellationRepository.delete(id);
	}

}
