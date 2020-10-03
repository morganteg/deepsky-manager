package it.attsd.deepsky.model;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;

@Repository
public class ConstellationRepository extends BaseRepository {

	@SuppressWarnings("unchecked")
	public List<Constellation> findAll() {
		Query query = entityManager.createQuery(String.format("SELECT t FROM %s t", Constellation.class.getName()));

		return (List<Constellation>) query.getResultList();
	}

	public Constellation findById(long id) throws RepositoryException {
		Constellation result = null;
		try {
			result = (Constellation) entityManager.find(Constellation.class, id);
		} catch (NoResultException e) {

		} catch (Exception e) {
			throw new RepositoryException(e);
		}

		return result;
	}

	public Constellation findByName(String name) throws RepositoryException {
		Constellation result = null;
		try {
			Query query = entityManager
					.createQuery(String.format("SELECT t FROM %s t WHERE t.name=:name", Constellation.class.getName()));
			query.setParameter("name", name.toLowerCase());
			result = (Constellation) query.getSingleResult();
		} catch (NoResultException e) {

		} catch (Exception e) {
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
			throw new RepositoryException(e);
		}

		return constellation;
	}

	@Transactional
	public void delete(long id) throws RepositoryException {
		try {
			Constellation constellation = findById(id);

			entityManager.remove(constellation);
			entityManager.flush();
		} catch (Exception e) {
			throw new RepositoryException(e);
		}
	}

}
