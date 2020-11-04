package it.attsd.deepsky.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "deepskyobjecttype")
public class DeepSkyObjectType {
	@Version
    private int version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true, nullable = false)
	private String type;

	public DeepSkyObjectType() {

	}

	public DeepSkyObjectType(String type) {
		this.type = type;
	}

	public DeepSkyObjectType(long id, String type) {
		this.id = id;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
