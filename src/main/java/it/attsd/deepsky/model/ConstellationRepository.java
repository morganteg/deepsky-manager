package it.attsd.deepsky.model;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.ConstellationAlreadyExistsException;

@Repository
public class ConstellationRepository extends BaseRepository {

	@Transactional
	public void emptyTable() {
		entityManager.createQuery(String.format("DELETE FROM %s", Constellation.class.getName())).executeUpdate();
		entityManager.flush();
	}

	@SuppressWarnings("unchecked")
	public List<Constellation> findAll() {
		Query query = entityManager.createQuery(String.format("SELECT t FROM %s t", Constellation.class.getName()));

		return query.getResultList();
	}

	public Constellation findById(long id) {
		return entityManager.find(Constellation.class, id);
	}

	public Constellation findByName(String name) {
		Constellation result = null;
		try {
			Query query = entityManager
					.createQuery(String.format("SELECT t FROM %s t WHERE t.name=:name", Constellation.class.getName()));
			query.setParameter("name", name.toLowerCase());
			result = (Constellation) query.getSingleResult();
		} catch (NoResultException e) {
			
		}
		return result;
	}

	@Transactional(rollbackFor = { ConstellationAlreadyExistsException.class, Exception.class })
	public Constellation save(Constellation constellation) throws ConstellationAlreadyExistsException {
		try {
			entityManager.persist(constellation);
			entityManager.flush();
		} catch (PersistenceException e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				throw new ConstellationAlreadyExistsException();
			}else {
				throw e;
			}
		}

		return constellation;
	}

	@Transactional
	public Constellation update(Constellation constellation) {
		Constellation constellationChanged = entityManager.merge(constellation);
		entityManager.flush();

		return constellationChanged;
	}

	@Transactional
	public void delete(long id) {
		Constellation constellation = findById(id);
		if (constellation != null) {
			entityManager.remove(constellation);
			entityManager.flush();
		}
	}

}
