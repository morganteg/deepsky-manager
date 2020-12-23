package it.attsd.deepsky.service;

import java.util.List;

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

	public DeepSkyObject save(DeepSkyObject deepSkyObject) {
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

//	/**
//	 * Creates a DeepSkyObject into database. If even just one of the provided
//	 * constellationId or deepSkyObjectTypeId, to which the DeepSkyObject would be
//	 * related, don't exist, a rollback is performed.
//	 * 
//	 * @param constellationId     the Constellation ID
//	 * @param deepSkyObjectTypeId the DeepSkyObjectType ID
//	 * @param deepSkyObjectName   the DeepSkyObject name
//	 * @return the created DeepSkyObject object
//	 * @throws ConstellationNotFoundException
//	 * @throws DeepSkyObjectTypeNotFoundException
//	 * @throws DeepSkyObjectAlreadyExistsException
//	 */
//	@Transactional(rollbackFor = { ConstellationAlreadyExistsException.class,
//			DeepSkyObjectTypeAlreadyExistsException.class, DeepSkyObjectAlreadyExistsException.class })
//	public DeepSkyObject save(long constellationId, long deepSkyObjectTypeId, String deepSkyObjectName)
//			throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException,
//			DeepSkyObjectAlreadyExistsException {
//		// 1 - Retrieve Constellation
//		Constellation constellation = constellationRepository.findById(constellationId).orElseGet(null);
//		if (constellation == null) {
//			throw new ConstellationNotFoundException();
//		}
//
//		// 2 - Retrieve DeepSkyObjectType
//		DeepSkyObjectType deepSkyObjectType = deepSkyObjectTypeRepository.findById(deepSkyObjectTypeId);
//		if (deepSkyObjectType == null) {
//			throw new DeepSkyObjectTypeNotFoundException();
//		}
//
//		// 3 - Save DeepSkyObject
//		return deepSkyObjectRepository.save(new DeepSkyObject(deepSkyObjectName, constellation));
//	}
//
//	/**
//	 * Creates a DeepSkyObject and contextually the related Constellation and
//	 * DeepSkyObjectType if they don't exist. If even just one of Constellation or
//	 * DeepSkyObjectType already exist, the operation is canceled and a rollback is
//	 * performed.
//	 * 
//	 * @param constellationName the Constellation name
//	 * @param deepSkyObjectName the DeepSkyObject name
//	 * @param deepSkyObjectType the DeepSkyObjectType name
//	 * @return the created DeepSkyObject
//	 * @throws ConstellationAlreadyExistsException
//	 * @throws DeepSkyObjectAlreadyExistsException
//	 * @throws DeepSkyObjectTypeAlreadyExistsException
//	 */
//	@Transactional(rollbackFor = { ConstellationAlreadyExistsException.class,
//			DeepSkyObjectTypeAlreadyExistsException.class })
//	public DeepSkyObject saveConstellationAndDeepSkyObject(String constellationName, String deepSkyObjectName,
//			String deepSkyObjectType) throws ConstellationAlreadyExistsException, DeepSkyObjectAlreadyExistsException,
//			DeepSkyObjectTypeAlreadyExistsException {
//		// 1 - Create Constellation
//		Constellation existingConstellation = constellationRepository.findByName(constellationName);
//		if (existingConstellation != null) {
//			throw new ConstellationAlreadyExistsException();
//		}
//		Constellation constellationToSave = new Constellation(constellationName);
//		Constellation constellationSaved = constellationRepository.save(constellationToSave);
//
//		// 3 - Create DeepSkyObject
//		DeepSkyObject deepSkyObject = new DeepSkyObject(deepSkyObjectName, constellationSaved);
//
//		return deepSkyObjectRepository.save(deepSkyObject);
//	}
//
//	@Transactional(rollbackFor = { DeepSkyObjectNotFoundException.class, ConstellationNotFoundException.class,
//			DeepSkyObjectTypeNotFoundException.class })
//	public DeepSkyObject update(long id, String name, long constellationId, long deepSkyObjectTypeId)
//			throws ConstellationNotFoundException, DeepSkyObjectTypeNotFoundException, DeepSkyObjectNotFoundException {
//		DeepSkyObject deepSkyObject = deepSkyObjectRepository.findById(id);
//		if (deepSkyObject == null) {
//			throw new DeepSkyObjectNotFoundException();
//		}
//
//		Constellation constellation = constellationRepository.findById(constellationId).orElseGet(null);
//		if (constellation == null) {
//			throw new ConstellationNotFoundException();
//		}
//
//		DeepSkyObjectType deepSkyObjectType = deepSkyObjectTypeRepository.findById(deepSkyObjectTypeId);
//		if (deepSkyObjectType == null) {
//			throw new DeepSkyObjectTypeNotFoundException();
//		}
//
//		deepSkyObject.setName(name);
//		deepSkyObject.setConstellation(constellation);
//
//		return deepSkyObjectRepository.update(deepSkyObject);
//	}

}
