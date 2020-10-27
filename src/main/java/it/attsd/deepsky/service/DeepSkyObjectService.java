package it.attsd.deepsky.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
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
		return deepSkyObjectRepository.findById(id);
	}

	public DeepSkyObject findByName(String name) {
		return deepSkyObjectRepository.findByName(name);
	}

	@Transactional
	public DeepSkyObject save(DeepSkyObject deepSkyObject) throws DeepSkyObjectAlreadyExistsException {
		return deepSkyObjectRepository.save(deepSkyObject);
	}

	@Transactional(rollbackOn = Exception.class)
	public DeepSkyObject save(long constellationId, long deepSkyObjectTypeId, String deepSkyObjectName)
			throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException,
			DeepSkyObjectAlreadyExistsException {
		// 1 - Create Constellation
		Constellation constellation = constellationRepository.findById(constellationId);
		if (constellation == null) {
			throw new ConstellationNotFoundException();
		}

		// 2 - Create DeepSkyObjectType
		DeepSkyObjectType deepSkyObjectType = deepSkyObjectTypeRepository.findById(deepSkyObjectTypeId);
		if (deepSkyObjectType == null) {
			throw new DeepSkyObjectTypeNotFoundException();
		}

		// 3 - Save DeepSkyObject
		DeepSkyObject deepSkyObject = new DeepSkyObject(deepSkyObjectName, constellation, deepSkyObjectType);
		DeepSkyObject deepSkyObjectSaved = deepSkyObjectRepository.save(deepSkyObject);

		return deepSkyObjectSaved;
	}

	@Transactional(rollbackOn = Exception.class)
	public DeepSkyObject saveConstellationAndDeepSkyObject(String constellationName, String deepSkyObjectName,
			String deepSkyObjectType) throws ConstellationAlreadyExistsException,
			DeepSkyObjectAlreadyExistsException, DeepSkyObjectTypeAlreadyExistsException {
		// 1 - Create Constellation
		Constellation existingConstellation = constellationRepository.findByName(constellationName);
		if (existingConstellation != null) {
			throw new ConstellationAlreadyExistsException();
		}
		Constellation constellationToSave = new Constellation(constellationName);
		Constellation constellationSaved = constellationRepository.save(constellationToSave);

		// 2 - Create DeepSkyObjectType
		DeepSkyObjectType existingDeepSkyObjectType = deepSkyObjectTypeRepository.findByType(deepSkyObjectType);
		if (existingDeepSkyObjectType != null) {
			throw new DeepSkyObjectTypeAlreadyExistsException();
		}
		DeepSkyObjectType deepSkyObjectTypeToSave = new DeepSkyObjectType(deepSkyObjectType);
		DeepSkyObjectType deepSkyObjectTypeSaved = deepSkyObjectTypeRepository.save(deepSkyObjectTypeToSave);

		// 3 - Create DeepSkyObject
//		DeepSkyObject existingDeepSkyObject = deepSkyObjectRepository.findByName(deepSkyObjectName);
//		if (existingDeepSkyObject != null) {
//			throw new DeepSkyObjectAlreadyExistsException();
//		}
		DeepSkyObject deepSkyObject = new DeepSkyObject(deepSkyObjectName, constellationSaved, deepSkyObjectTypeSaved);
		DeepSkyObject deepSkyObjectSaved = deepSkyObjectRepository.save(deepSkyObject);

		return deepSkyObjectSaved;
	}

	public DeepSkyObject update(DeepSkyObject deepSkyObject) {
		return deepSkyObjectRepository.update(deepSkyObject);
	}

	public void delete(long id) {
		deepSkyObjectRepository.delete(id);
	}

}
