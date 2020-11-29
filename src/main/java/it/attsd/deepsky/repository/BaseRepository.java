package it.attsd.deepsky.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Deprecated
public abstract class BaseRepository {

	@PersistenceContext(name = "it.attsd.deepsky")
	protected EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
