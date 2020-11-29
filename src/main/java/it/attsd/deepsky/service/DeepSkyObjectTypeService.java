package it.attsd.deepsky.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.DeepSkyObjectType;
import it.attsd.deepsky.repository.DeepSkyObjectTypeRepository;

@Service
public class DeepSkyObjectTypeService {

	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	public List<DeepSkyObjectType> findAll() {
		return deepSkyObjectTypeRepository.findAll();
	}

	public DeepSkyObjectType findById(long id) throws DeepSkyObjectTypeNotFoundException {
		DeepSkyObjectType deepSkyObjectType = deepSkyObjectTypeRepository.findById(id);
		if(deepSkyObjectType == null) {
			throw new DeepSkyObjectTypeNotFoundException();
		}
		
		return deepSkyObjectType;
	}

	public DeepSkyObjectType findByType(String type) {
		return deepSkyObjectTypeRepository.findByType(type);
	}

	public DeepSkyObjectType save(DeepSkyObjectType deepSkyObjectType) throws DeepSkyObjectTypeAlreadyExistsException {
		return deepSkyObjectTypeRepository.save(deepSkyObjectType);
	}

	public DeepSkyObjectType update(DeepSkyObjectType deepSkyObjectType) {
		return deepSkyObjectTypeRepository.update(deepSkyObjectType);

	}

	public void delete(long id) {
		deepSkyObjectTypeRepository.delete(id);
	}

}
