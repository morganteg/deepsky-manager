package it.attsd.deepsky.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exception.ConstellationNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectEmptyAttributeException;
import it.attsd.deepsky.exception.DeepSkyObjectNotFoundException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;
import it.attsd.deepsky.exception.DeepSkyObjectTypeNotFoundException;
import it.attsd.deepsky.model.ConstellationRepository;
import it.attsd.deepsky.model.DeepSkyObjectRepository;
import it.attsd.deepsky.model.DeepSkyObjectTypeRepository;

@Service
public class DeepSkyObjectService {

	@Autowired
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

	public DeepSkyObject findById(long id) throws DeepSkyObjectNotFoundException {
		DeepSkyObject deepSkyObject = deepSkyObjectRepository.findById(id);
		if(deepSkyObject == null) {
			throw new DeepSkyObjectNotFoundException();
		}
		return deepSkyObject;
	}

	public DeepSkyObject findByName(String name) {
		return deepSkyObjectRepository.findByName(name);
	}

	@Transactional
	public DeepSkyObject save(DeepSkyObject deepSkyObject) throws DeepSkyObjectAlreadyExistsException {
		return deepSkyObjectRepository.save(deepSkyObject);
	}

	@Transactional(rollbackFor = { ConstellationAlreadyExistsException.class,
			DeepSkyObjectTypeAlreadyExistsException.class, DeepSkyObjectAlreadyExistsException.class })
	public DeepSkyObject save(long constellationId, long deepSkyObjectTypeId, String deepSkyObjectName)
			throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException,
			DeepSkyObjectAlreadyExistsException {
		// 1 - Retrieve Constellation
		Constellation constellation = constellationRepository.findById(constellationId);
		if (constellation == null) {
			throw new ConstellationNotFoundException();
		}

		// 2 - Retrieve DeepSkyObjectType
		DeepSkyObjectType deepSkyObjectType = deepSkyObjectTypeRepository.findById(deepSkyObjectTypeId);
		if (deepSkyObjectType == null) {
			throw new DeepSkyObjectTypeNotFoundException();
		}

		// 3 - Save DeepSkyObject
		return deepSkyObjectRepository.save(new DeepSkyObject(deepSkyObjectName, constellation, deepSkyObjectType));
	}

	@Transactional(rollbackFor = { ConstellationAlreadyExistsException.class,
			DeepSkyObjectTypeAlreadyExistsException.class })
	public DeepSkyObject saveConstellationAndDeepSkyObject(String constellationName, String deepSkyObjectName,
			String deepSkyObjectType) throws ConstellationAlreadyExistsException, DeepSkyObjectAlreadyExistsException,
			DeepSkyObjectTypeAlreadyExistsException {
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
		DeepSkyObject deepSkyObject = new DeepSkyObject(deepSkyObjectName, constellationSaved, deepSkyObjectTypeSaved);
		
		return deepSkyObjectRepository.save(deepSkyObject);
	}

	@Transactional(rollbackFor = { DeepSkyObjectNotFoundException.class, ConstellationNotFoundException.class,
			DeepSkyObjectTypeNotFoundException.class })
	public DeepSkyObject update(long id, String name, long constellationId, long deepSkyObjectTypeId)
			throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, DeepSkyObjectNotFoundException,
			DeepSkyObjectEmptyAttributeException {
		DeepSkyObject deepSkyObject = findById(id);
		if (deepSkyObject == null) {
			throw new DeepSkyObjectNotFoundException();
		}

		Constellation constellation = constellationRepository.findById(constellationId);
		if (constellation == null) {
			throw new ConstellationNotFoundException();
		}

		DeepSkyObjectType deepSkyObjectType = deepSkyObjectTypeRepository.findById(deepSkyObjectTypeId);
		if (deepSkyObjectType == null) {
			throw new DeepSkyObjectTypeNotFoundException();
		}

		deepSkyObject.setName(name);
		deepSkyObject.setConstellation(constellation);
		deepSkyObject.setType(deepSkyObjectType);
		
		if(StringUtils.isEmpty(deepSkyObject.getName())) {
			throw new DeepSkyObjectEmptyAttributeException("name");
		}
		if(deepSkyObject.getConstellation() == null) {
			throw new DeepSkyObjectEmptyAttributeException("constellation");
		}
		if(deepSkyObject.getType() == null) {
			throw new DeepSkyObjectEmptyAttributeException("type");
		}

		return deepSkyObjectRepository.update(deepSkyObject);
	}

	public void delete(long id) {
		deepSkyObjectRepository.delete(id);
	}

}
