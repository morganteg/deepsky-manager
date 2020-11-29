package it.attsd.deepsky.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.repository.ConstellationRepository;

@Service
public class ConstellationService {

	private ConstellationRepository constellationRepository;
	
	public ConstellationService(ConstellationRepository constellationRepository) {
		this.constellationRepository = constellationRepository;
	}

	public List<Constellation> findAll() {
		return constellationRepository.findAll();
	}

	public Constellation findById(long id) {
		return constellationRepository.findById(id).orElse(null);
	}

	public Constellation findByName(String name) {
		return constellationRepository.findByName(name);
	}

	public Constellation save(Constellation constellation) {
		constellation.setId(null);
		return constellationRepository.save(constellation);
	}

	public Constellation updateById(long id, Constellation constellation) {
		constellation.setId(id);
		return constellationRepository.save(constellation);
	}

	public void deleteById(long id) {
		constellationRepository.deleteById(id);
	}

}
