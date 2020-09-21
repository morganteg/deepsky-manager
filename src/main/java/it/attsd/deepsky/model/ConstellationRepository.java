package it.attsd.deepsky.model;

import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;

@Transactional
@Repository
public class ConstellationRepository extends BaseRepository implements IConstellationRepository {

	public List<Constellation> findAll() {
		Query query = entityManager.createQuery(String.format("SELECT c FROM %s c", Constellation.TABLE_NAME));
		
		return (List<Constellation>)query.getResultList();
	}

	public Constellation findById(long id) {
		Query query = entityManager.createQuery(String.format("SELECT c FROM %s c WHERE c.id=:id", Constellation.TABLE_NAME));
		query.setParameter("id", id);
		Constellation result = (Constellation)query.getSingleResult();
		
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

}
