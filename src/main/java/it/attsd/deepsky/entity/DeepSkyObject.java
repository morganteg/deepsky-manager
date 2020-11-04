package it.attsd.deepsky.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "deepskyobject")
public class DeepSkyObject {
	@Version
    private int version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToOne
	private Constellation constellation;
	
	@ManyToOne
	private DeepSkyObjectType type;

	public DeepSkyObject() {

	}

	public DeepSkyObject(String name, Constellation constellation, DeepSkyObjectType type) {
		this.name = name;
		this.constellation = constellation;
		this.type = type;
	}

	public DeepSkyObject(long id, String name, Constellation constellation, DeepSkyObjectType type) {
		this.id = id;
		this.name = name;
		this.constellation = constellation;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Constellation getConstellation() {
		return constellation;
	}

	public void setConstellation(Constellation constellation) {
		this.constellation = constellation;
	}

	public DeepSkyObjectType getType() {
		return type;
	}

	public void setType(DeepSkyObjectType type) {
		this.type = type;
	}

}
