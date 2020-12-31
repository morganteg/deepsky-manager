package it.attsd.deepsky.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "deepskyobject")
public class DeepSkyObject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String name;

	@ManyToOne(optional = false)
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

	public Constellation getConstellation() {
		return constellation;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, constellation);
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
