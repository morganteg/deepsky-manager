package it.attsd.deepsky.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Constellation
 *
 */
@Entity
@Table(name = "CONSTELLATION")
public class Constellation {
	public static final String TABLE_NAME = "CONSTELLATION";
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	
	public Constellation() {
		
	}
	
	public Constellation(String name) {
		this.name = name;
	}
	
	public Constellation(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
