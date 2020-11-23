package it.attsd.deepsky.model;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.exception.DeepSkyObjectAlreadyExistsException;

@Repository
public class DeepSkyObjectRepository extends BaseRepository {
	@Transactional
	public void emptyTable() {
		entityManager.createQuery(String.format("DELETE FROM %s", DeepSkyObject.class.getName())).executeUpdate();
		entityManager.flush();
	}

	@SuppressWarnings("unchecked")
	public List<DeepSkyObject> findAll() {
		Query query = entityManager.createQuery(String.format("SELECT t FROM %s t", DeepSkyObject.class.getName()));

		return query.getResultList();
	}

	public DeepSkyObject findById(long id) {
		return entityManager.find(DeepSkyObject.class, id);
	}

	public DeepSkyObject findByName(String name) {
		DeepSkyObject result = null;
		try {
			Query query = entityManager
					.createQuery(String.format("SELECT t FROM %s t WHERE t.name=:name", DeepSkyObject.class.getName()));
			query.setParameter("name", name.toLowerCase());
			result = (DeepSkyObject) query.getSingleResult();
		} catch (NoResultException e) {
			// NoResultException
		}
		return result;
	}

	@Transactional(rollbackFor = { DeepSkyObjectAlreadyExistsException.class, Exception.class })
	public DeepSkyObject save(DeepSkyObject deepSkyObject) throws DeepSkyObjectAlreadyExistsException {
		try {
			entityManager.persist(deepSkyObject);
			entityManager.flush();
		} catch (PersistenceException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				throw new DeepSkyObjectAlreadyExistsException();
			} else {
				throw e;
			}
		}

		return deepSkyObject;
	}

	@Transactional
	public DeepSkyObject update(DeepSkyObject deepSkyObject) {
		DeepSkyObject deepSkyObjectChanged = entityManager.merge(deepSkyObject);
		entityManager.flush();

		return deepSkyObjectChanged;
	}

	@Transactional
	public void delete(long id) {
		DeepSkyObject deepSkyObject = findById(id);
		if (deepSkyObject != null) {
			entityManager.remove(deepSkyObject);
			entityManager.flush();
		}
	}

}
