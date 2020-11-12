package it.attsd.deepsky.model;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.DeepSkyObjectTypeAlreadyExistsException;

@Repository
public class DeepSkyObjectTypeRepository extends BaseRepository {

	@Transactional
	public void emptyTable() {
		entityManager.createQuery(String.format("DELETE FROM %s", DeepSkyObjectType.class.getName())).executeUpdate();
		entityManager.flush();
	}

	@SuppressWarnings("unchecked")
	public List<DeepSkyObjectType> findAll() {
		Query query = entityManager.createQuery(String.format("SELECT t FROM %s t", DeepSkyObjectType.class.getName()));

		return query.getResultList();
	}

	public DeepSkyObjectType findById(long id) {
		return entityManager.find(DeepSkyObjectType.class, id);
	}

	public DeepSkyObjectType findByType(String type) {
		DeepSkyObjectType result = null;
		try {
			Query query = entityManager.createQuery(
					String.format("SELECT t FROM %s t WHERE t.type=:type", DeepSkyObjectType.class.getName()));
			query.setParameter("type", type.toLowerCase());
			result = (DeepSkyObjectType) query.getSingleResult();
		} catch (NoResultException e) {

		}
		return result;
	}

	@Transactional(rollbackFor = { DeepSkyObjectTypeAlreadyExistsException.class, Exception.class })
	public DeepSkyObjectType save(DeepSkyObjectType deepSkyObjectType) throws DeepSkyObjectTypeAlreadyExistsException {
		try {
			entityManager.persist(deepSkyObjectType);
			entityManager.flush();
		} catch (PersistenceException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				throw new DeepSkyObjectTypeAlreadyExistsException();
			} else {
				throw e;
			}
		}

		return deepSkyObjectType;
	}

	@Transactional
	public DeepSkyObjectType update(DeepSkyObjectType deepSkyObjectType) {
		DeepSkyObjectType deepSkyObjectTypeChanged = entityManager.merge(deepSkyObjectType);
		entityManager.flush();

		return deepSkyObjectTypeChanged;
	}

	@Transactional
	public void delete(long id) {
		DeepSkyObjectType deepSkyObjectType = findById(id);
		if (deepSkyObjectType != null) {
			entityManager.remove(deepSkyObjectType);
			entityManager.flush();
		}
	}

}
