package it.attsd.deepsky.model;

import java.util.Objects;

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
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToOne
	private Constellation constellation;

	public DeepSkyObject() {

	}

	public DeepSkyObject(String name, Constellation constellation) {
		this.name = name;
		this.constellation = constellation;
	}

	public DeepSkyObject(Long id, String name, Constellation constellation) {
		this.id = id;
		this.name = name;
		this.constellation = constellation;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeepSkyObject other = (DeepSkyObject) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name) && Objects.equals(constellation, other.constellation);
	}

}
