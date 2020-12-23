package it.attsd.deepsky.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.attsd.deepsky.model.DeepSkyObject;

public interface DeepSkyObjectRepository extends JpaRepository<DeepSkyObject, Long> {
	
	DeepSkyObject findByName(String name);

}
