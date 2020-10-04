package it.attsd.deepsky.model;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import it.attsd.deepsky.entity.DeepSkyObjectType;
import it.attsd.deepsky.exception.RepositoryException;

@Repository
public class DeepSkyObjectTypeRepository extends BaseRepository {

	@Transactional
	public void emptyTable() {
		entityManager.createQuery(String.format("DELETE FROM %s", DeepSkyObjectType.class.getName())).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<DeepSkyObjectType> findAll() {
		Query query = entityManager.createQuery(String.format("SELECT t FROM %s t", DeepSkyObjectType.class.getName()));

		return (List<DeepSkyObjectType>) query.getResultList();
	}

	public DeepSkyObjectType findById(long id) throws RepositoryException {
		DeepSkyObjectType result = null;
		try {
			result = (DeepSkyObjectType) entityManager.find(DeepSkyObjectType.class, id);
		} catch (NoResultException e) {

		} catch (Exception e) {
			throw new RepositoryException(e);
		}

		return result;
	}

	public DeepSkyObjectType findByType(String type) throws RepositoryException {
		DeepSkyObjectType result = null;
		try {
			Query query = entityManager.createQuery(
					String.format("SELECT t FROM %s t WHERE t.type=:type", DeepSkyObjectType.class.getName()));
			query.setParameter("type", type.toLowerCase());
			result = (DeepSkyObjectType) query.getSingleResult();
		} catch (NoResultException e) {

		} catch (Exception e) {
			throw new RepositoryException(e);
		}
		return result;
	}

	@Transactional
	public DeepSkyObjectType save(DeepSkyObjectType deepSkyObjectType) throws RepositoryException {
		try {
			entityManager.persist(deepSkyObjectType);
			entityManager.flush();
		} catch (Exception e) {
			throw new RepositoryException(e);
		}

		return deepSkyObjectType;
	}

	@Transactional
	public void delete(long id) throws RepositoryException {
		try {
			DeepSkyObjectType deepSkyObjectType = findById(id);

			entityManager.remove(deepSkyObjectType);
			entityManager.flush();
		} catch (Exception e) {
			throw new RepositoryException(e);
		}
	}

}
