package it.attsd.deepsky.repository;

import it.attsd.deepsky.model.Constellation;
import org.springframework.data.jpa.repository.JpaRepository;

import it.attsd.deepsky.model.DeepSkyObject;

import java.util.List;

public interface DeepSkyObjectRepository extends JpaRepository<DeepSkyObject, Long> {
	
	DeepSkyObject findByName(String name);

	List<DeepSkyObject> findByConstellation(Constellation constellation);

}
