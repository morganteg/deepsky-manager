package it.attsd.deepsky.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.attsd.deepsky.entity.Constellation;
import it.attsd.deepsky.exception.RepositoryException;
import it.attsd.deepsky.model.ConstellationRepository;

@RestController
public class ConstellationController {
	@Autowired
	private ConstellationRepository constellationRepository;
	
	@GetMapping("/constellations")
	public String getAll() {
//		Constellation orionToSave = new Constellation("orion");
//		try {
//			Constellation orionSaved = constellationRepository.save(orionToSave);
//		} catch (RepositoryException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return "Ok";
	}

}
