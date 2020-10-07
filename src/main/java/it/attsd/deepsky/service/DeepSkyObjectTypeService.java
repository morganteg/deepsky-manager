package it.attsd.deepsky.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;

@Service
public class DeepSkyObjectTypeService {

	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;

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

	@Transactional
	public DeepSkyObjectType save(DeepSkyObjectType deepSkyObjectType) {
		DeepSkyObjectType deepSkyObjectTypeSaved = null;
		try {
			deepSkyObjectTypeSaved = deepSkyObjectTypeRepository.save(deepSkyObjectType);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return deepSkyObjectTypeSaved;
	}

	@Transactional
	public void update(DeepSkyObjectType deepSkyObjectType) {
		// TODO Auto-generated method stub

	}

	@Transactional
	public void delete(long id) {
		try {
			deepSkyObjectTypeRepository.delete(id);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
