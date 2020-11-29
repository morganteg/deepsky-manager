package it.attsd.deepsky.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * Constellation
 *
 */
@Entity
@Table(name = "constellation")
public class Constellation {
	@Version
    private int version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	public Constellation() {

	}
	
	public Constellation(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Constellation(String name) {
		this.name = name;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
