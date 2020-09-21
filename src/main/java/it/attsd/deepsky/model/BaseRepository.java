package it.attsd.deepsky.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BaseRepository {

	@PersistenceContext(name = "it.attsd.deepsky")
	protected EntityManager entityManager;

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
