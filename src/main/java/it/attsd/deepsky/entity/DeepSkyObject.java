package it.attsd.deepsky.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "deepskyobject")
public class DeepSkyObject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true, nullable = false)
	private String name;

	@OneToOne(optional = false)
	private Constellation constellation;
	
	@OneToOne(optional = false)
	private DeepSkyObjectType type;

//	@Column(nullable = false)
//	private int raHours;
//	
//	@Column(nullable = false)
//	private int raMinutes;
//	
//	@Column(nullable = false)
//	private float raSeconds;
//
//	@Column(nullable = false)
//	private boolean decPositive;
//	
//	@Column(nullable = false)
//	private int decDegree;
//	
//	@Column(nullable = false)
//	private int decMinutes;
//	
//	@Column(nullable = false)
//	private float decSeconds;

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

	public void setId(long id) {
		this.id = id;
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
