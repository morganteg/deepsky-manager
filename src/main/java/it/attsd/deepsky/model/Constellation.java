package it.attsd.deepsky.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Constellation
 *
 */
@Entity
@Table(name = "constellation")
public class Constellation {
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

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Constellation other = (Constellation) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

}
