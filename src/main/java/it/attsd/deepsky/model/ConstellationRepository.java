package it.attsd.deepsky.model;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;

@Repository
public class ConstellationRepository extends BaseRepository {
	private Logger logger = LoggerFactory.getLogger(ConstellationRepository.class);
	
	@Transactional
	public void emptyTable() {
		entityManager.createQuery(String.format("DELETE FROM %s", Constellation.class.getName())).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public List<Constellation> findAll() {
		Query query = entityManager.createQuery(String.format("SELECT t FROM %s t", Constellation.class.getName()));

		return (List<Constellation>) query.getResultList();
	}

	public Constellation findById(long id) {
		return entityManager.find(Constellation.class, id);
	}

	public Constellation findByName(String name) throws RepositoryException {
		Constellation result = null;
		try {
			Query query = entityManager
					.createQuery(String.format("SELECT t FROM %s t WHERE t.name=:name", Constellation.class.getName()));
			query.setParameter("name", name.toLowerCase());
			result = (Constellation) query.getSingleResult();
		} catch (NoResultException e) {
			logger.info(String.format("No Constellation found with name %s", name));
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RepositoryException(e);
		}
		return result;
	}

	@Transactional
	public Constellation save(Constellation constellation) throws RepositoryException {
		try {
			entityManager.persist(constellation);
			entityManager.flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RepositoryException(e);
		}

		return constellation;
	}
	
	@Transactional
	public void update(Constellation constellation) throws RepositoryException {
		try {
			entityManager.merge(constellation);
			entityManager.flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RepositoryException(e);
		}
	}

	@Transactional
	public void delete(long id) throws RepositoryException {
		try {
			Constellation constellation = findById(id);

			entityManager.remove(constellation);
			entityManager.flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RepositoryException(e);
		}
	}

}
