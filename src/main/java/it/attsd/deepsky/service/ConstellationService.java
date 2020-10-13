package it.attsd.deepsky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;

@Service
public class ConstellationService {
	
	@Autowired
	private ConstellationRepository constellationRepository;
	
	public List<Constellation> findAll() {
		return constellationRepository.findAll();
	}
	
	public Constellation findById(long id) {
		return constellationRepository.findById(id);
	}
	
	public Constellation findByName(String name) {
		Constellation constellation = null;
		try {
			constellation = constellationRepository.findByName(name);
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		return constellation;
	}


	public Constellation save(Constellation constellation) {
		Constellation constellationSaved = null;
		try {
			constellationSaved = constellationRepository.save(constellation);
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
		return constellationSaved;
	}


	public void update(Constellation constellation) {
		try {
			constellationRepository.update(constellation);
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		
	}


	public void delete(long id) {
		try {
			constellationRepository.delete(id);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
