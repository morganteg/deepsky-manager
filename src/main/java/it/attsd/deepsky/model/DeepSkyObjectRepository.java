package it.attsd.deepsky.model;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import it.attsd.deepsky.entity.DeepSkyObject;
import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.RepositoryException;

@Repository
public class DeepSkyObjectRepository extends BaseRepository {
	private Logger logger = LoggerFactory.getLogger(DeepSkyObjectRepository.class);
	
	@Transactional
	public void emptyTable() {
		entityManager.createQuery(String.format("DELETE FROM %s", DeepSkyObject.class.getName())).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<DeepSkyObject> findAll() {
		Query query = entityManager.createQuery(String.format("SELECT t FROM %s t", DeepSkyObject.class.getName()));

		return (List<DeepSkyObject>) query.getResultList();
	}

	public DeepSkyObject findById(long id) {
		return entityManager.find(DeepSkyObject.class, id);
	}

	public DeepSkyObject findByName(String name) throws RepositoryException {
		DeepSkyObject result = null;
		try {
			Query query = entityManager
					.createQuery(String.format("SELECT t FROM %s t WHERE t.name=:name", DeepSkyObject.class.getName()));
			query.setParameter("name", name.toLowerCase());
			result = (DeepSkyObject) query.getSingleResult();
		} catch (NoResultException e) {
			logger.info(String.format("No DeepSkyObject found with name %s", name));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RepositoryException(e);
		}
		return result;
	}

	@Transactional
	public DeepSkyObject save(DeepSkyObject deepSkyObject) throws RepositoryException {
		try {
			entityManager.persist(deepSkyObject);
			entityManager.flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RepositoryException(e);
		}

		return deepSkyObject;
	}
	
	@Transactional
	public void update(DeepSkyObject deepSkyObject) throws RepositoryException {
		try {
			entityManager.merge(deepSkyObject);
			entityManager.flush();
		} catch (Exception e) {
//			logger.error(e.getMessage());
			throw new RepositoryException(e);
		}
	}

	@Transactional
	public void delete(long id) throws RepositoryException {
		try {
			DeepSkyObject deepSkyObject = findById(id);

			entityManager.remove(deepSkyObject);
			entityManager.flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RepositoryException(e);
		}
	}

}
