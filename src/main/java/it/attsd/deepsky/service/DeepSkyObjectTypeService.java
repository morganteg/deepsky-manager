package it.attsd.deepsky.service;

import java.util.List;

import org.springframework.stereotype.Service;

import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;

@Service
public class DeepSkyObjectTypeService {

	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

	public DeepSkyObjectTypeService(DeepSkyObjectTypeRepository deepSkyObjectTypeRepository) {
		this.deepSkyObjectTypeRepository = deepSkyObjectTypeRepository;
	}

	public List<DeepSkyObjectType> findAll() {
		return deepSkyObjectTypeRepository.findAll();
	}

	public DeepSkyObjectType findById(long id) {
		DeepSkyObjectType deepSkyObjectType = null;
		try {
			deepSkyObjectType = deepSkyObjectTypeRepository.findById(id);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deepSkyObjectType;
	}

	public DeepSkyObjectType findByType(String type) {
		DeepSkyObjectType deepSkyObjectType = null;
		try {
			deepSkyObjectType = deepSkyObjectTypeRepository.findByType(type);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deepSkyObjectType;
	}

	public DeepSkyObjectType save(DeepSkyObjectType deepSkyObject) {
		DeepSkyObjectType deepSkyObjectSaved = null;
		try {
			deepSkyObjectSaved = deepSkyObjectTypeRepository.save(deepSkyObject);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deepSkyObjectSaved;
	}

	public void update(DeepSkyObjectType deepSkyObjectType) {
		// TODO Auto-generated method stub

	}

	public void delete(long id) {
		try {
			deepSkyObjectTypeRepository.delete(id);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
