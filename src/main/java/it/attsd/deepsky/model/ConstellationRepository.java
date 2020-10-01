package it.attsd.deepsky.model;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;

@Transactional
@Repository
public class ConstellationRepository extends BaseRepository implements IConstellationRepository {
	
	@Transactional
	public void dropCreateSchema() {
		entityManager.createNativeQuery(String.format("DROP DATABASE IF EXISTS %s", "deepskymanager")).executeUpdate();
		entityManager.createNativeQuery(String.format("CREATE SCHEMA %s", "deepskymanager")).executeUpdate();
	}
	
	public void emptyTable() {
		entityManager.createNativeQuery(String.format("DELETE FROM %s", Constellation.TABLE_NAME)).executeUpdate();
	}
	
	public List<Constellation> findAll() {
		Query query = entityManager.createQuery(String.format("SELECT c FROM %s c", Constellation.TABLE_NAME));

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
					.createQuery(String.format("SELECT c FROM %s c WHERE c.name=:name", Constellation.TABLE_NAME));
			query.setParameter("name", name.toLowerCase());
			result = (Constellation) query.getSingleResult();
		} catch (NoResultException e) {

		} catch (Exception e) {
			throw new RepositoryException(e);
		}
		return result;
	}

	public Constellation save(Constellation constellation) throws RepositoryException {
		try {
			entityManager.persist(constellation);
			entityManager.flush();
		} catch (Exception e) {
			throw new RepositoryException(e);
		}

		return constellation;
	}

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
