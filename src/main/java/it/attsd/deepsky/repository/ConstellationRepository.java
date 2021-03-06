package it.attsd.deepsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.attsd.deepsky.model.Constellation;

public interface ConstellationRepository extends JpaRepository<Constellation, Long> {
	
	Constellation findByName(String name);

}
