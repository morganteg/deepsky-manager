package it.attsd.deepsky.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;

@Service
public class ConstellationService {
	
	private ConstellationRepository constellationRepository;
	
	public ConstellationService(ConstellationRepository constellationRepository) {
		this.constellationRepository = constellationRepository;
	}
	
	
	public List<Constellation> getAll() {
		return constellationRepository.findAll();
	}
	
	public Constellation getById(long id) {
		Constellation constellation = null;
		try {
			constellation = constellationRepository.findById(id);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return constellation;
	}

}
