package it.attsd.deepsky.model;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public abstract class BaseRepository {

	@PersistenceContext(name = "it.attsd.deepsky")
	protected EntityManager entityManager;

	public EntityManager getEntityManager() {
		return this.entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Transactional
	public void emptyTable(String tableName) {
		entityManager.createNativeQuery(String.format("DELETE FROM %s", tableName)).executeUpdate();
	}

}
