package it.attsd.deepsky.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;

@Service
public class DeepSkyObjectService {
	
	private DeepSkyObjectRepository deepSkyObjectRepository;
	
	@Autowired
	private ConstellationRepository constellationRepository;
	
	@Autowired
	private DeepSkyObjectTypeRepository deepSkyObjectTypeRepository;
	
	public DeepSkyObjectService(DeepSkyObjectRepository deepSkyObjectRepository) {
		this.deepSkyObjectRepository = deepSkyObjectRepository;
	}
	
	public List<DeepSkyObject> findAll() {
		return deepSkyObjectRepository.findAll();
	}
	
	public DeepSkyObject findById(long id) {
		DeepSkyObject constellation = null;
		try {
			constellation = deepSkyObjectRepository.findById(id);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return constellation;
	}
	
	public DeepSkyObject findByName(String name) {
		DeepSkyObject constellation = null;
		try {
			constellation = deepSkyObjectRepository.findByName(name);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return constellation;
	}


	public DeepSkyObject save(DeepSkyObject deepSkyObject) {
		DeepSkyObject deepSkyObjectSaved = null;
		try {
			deepSkyObjectSaved = deepSkyObjectRepository.save(deepSkyObject);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return deepSkyObjectSaved;
	}


	public void update(DeepSkyObject constellation) {
		// TODO Auto-generated method stub
		
	}


	public void delete(long id) {
		try {
			deepSkyObjectRepository.delete(id);
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Transactional(rollbackOn = Exception.class)
	public DeepSkyObject saveConstellationAndDeepSkyObject(String constellationName, String deepSkyObjectName, String deepSkyObjectType) throws ConstellationAlreadyExistsException, RepositoryException, DeepSkyObjectAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException {
		// 1 - Create Constellation
		Constellation existingConstellation = constellationRepository.findByName(constellationName);
		if(existingConstellation != null) {
			throw new ConstellationAlreadyExistsException();
		}
		Constellation constellationToSave = new Constellation(constellationName);
		Constellation constellationSaved = constellationRepository.save(constellationToSave);
		if(constellationSaved == null || constellationSaved.getId() == 0) {
			throw new RepositoryException("Error saving Constellation");
		}
		
		// 2 - Create DeepSkyObjectType
		DeepSkyObjectType existingDeepSkyObjectType = deepSkyObjectTypeRepository.findByType(deepSkyObjectType);
		if(existingDeepSkyObjectType != null) {
			throw new DeepSkyObjectTypeAlreadyExistsException();
		}
		DeepSkyObjectType deepSkyObjectTypeToSave = new DeepSkyObjectType(deepSkyObjectType);
		DeepSkyObjectType deepSkyObjectTypeSaved = deepSkyObjectTypeRepository.save(deepSkyObjectTypeToSave);
		if(deepSkyObjectTypeSaved == null || deepSkyObjectTypeSaved.getId() == 0) {
			throw new RepositoryException("Error saving Constellation");
		}

		// 3 - Create DeepSkyObject
		DeepSkyObject existingDeepSkyObject = deepSkyObjectRepository.findByName(deepSkyObjectName);
		if(existingDeepSkyObject != null) {
			throw new DeepSkyObjectAlreadyExistsException();
		}
		DeepSkyObject deepSkyObject = new DeepSkyObject(deepSkyObjectName, constellationSaved, deepSkyObjectTypeSaved);
		DeepSkyObject deepSkyObjectSaved = deepSkyObjectRepository.save(deepSkyObject);
		if(deepSkyObjectSaved == null || deepSkyObjectSaved.getId() == 0) {
			throw new RepositoryException("Error saving DeepSkyObject");
		}
		
		return deepSkyObjectSaved;
	}

}
