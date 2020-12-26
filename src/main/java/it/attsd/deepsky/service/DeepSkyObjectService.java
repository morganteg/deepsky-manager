package it.attsd.deepsky.service;

import java.util.List;

import it.attsd.deepsky.exceptions.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exceptions.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.model.Constellation;
import org.springframework.stereotype.Service;

import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.repository.DeepSkyObjectRepository;

@Service
public class DeepSkyObjectService {

	private DeepSkyObjectRepository deepSkyObjectRepository;
	
	public DeepSkyObjectService(DeepSkyObjectRepository deepSkyObjectRepository) {
		this.deepSkyObjectRepository = deepSkyObjectRepository;
	}

	public List<DeepSkyObject> findAll() {
		return deepSkyObjectRepository.findAll();
	}

	public DeepSkyObject findById(long id) {
		return deepSkyObjectRepository.findById(id).orElse(null);
	}

	public DeepSkyObject findByName(String name) {
		return deepSkyObjectRepository.findByName(name);
	}

	public List<DeepSkyObject> findByConstellation(Constellation constellation) {
		return deepSkyObjectRepository.findByConstellation(constellation);
	}

	public DeepSkyObject save(DeepSkyObject deepSkyObject) throws DeepSkyObjectAlreadyExistsException {
		DeepSkyObject existingDeepSkyObject = deepSkyObjectRepository.findByName(deepSkyObject.getName());
		if(existingDeepSkyObject != null){
			throw new DeepSkyObjectAlreadyExistsException();
		}
		deepSkyObject.setId(null);
		return deepSkyObjectRepository.save(deepSkyObject);
	}

	public DeepSkyObject updateById(long id, DeepSkyObject deepSkyObject) {
		deepSkyObject.setId(id);
		return deepSkyObjectRepository.save(deepSkyObject);
	}

	public void deleteById(long id) {
		deepSkyObjectRepository.deleteById(id);
	}

}
