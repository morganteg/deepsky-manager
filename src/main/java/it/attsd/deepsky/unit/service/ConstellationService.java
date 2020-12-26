package it.attsd.deepsky.unit.service;

import java.util.List;

import it.attsd.deepsky.exceptions.ConstellationAlreadyExistsException;
import it.attsd.deepsky.exceptions.ConstellationIsStillUsedException;
import it.attsd.deepsky.model.DeepSkyObject;
import it.attsd.deepsky.unit.repository.DeepSkyObjectRepository;
import org.springframework.stereotype.Service;

import it.attsd.deepsky.model.Constellation;
import it.attsd.deepsky.unit.repository.ConstellationRepository;

@Service
public class ConstellationService {

    private ConstellationRepository constellationRepository;

    private DeepSkyObjectRepository deepSkyObjectRepository;

    public ConstellationService(ConstellationRepository constellationRepository, DeepSkyObjectRepository deepSkyObjectRepository) {
        this.constellationRepository = constellationRepository;
        this.deepSkyObjectRepository = deepSkyObjectRepository;
    }

    public List<Constellation> findAll() {
        return constellationRepository.findAll();
    }

    public Constellation findById(long id) {
        return constellationRepository.findById(id).orElse(null);
    }

    public Constellation findByName(String name) {
        return constellationRepository.findByName(name);
    }

    public Constellation save(Constellation constellation) throws ConstellationAlreadyExistsException {
        Constellation existingConstellation = constellationRepository.findByName(constellation.getName());
        if (existingConstellation != null) {
            throw new ConstellationAlreadyExistsException();
        }
        constellation.setId(null);
        return constellationRepository.save(constellation);
    }

    public Constellation updateById(long id, Constellation constellation) {
        constellation.setId(id);
        return constellationRepository.save(constellation);
    }

    public void deleteById(long id) throws ConstellationIsStillUsedException {
        Constellation existingConstellation = constellationRepository.findById(id).orElse(null);
        if (existingConstellation != null) {
            List<DeepSkyObject> deepSkyObjects = deepSkyObjectRepository.findByConstellation(existingConstellation);
            if (!deepSkyObjects.isEmpty()) {
                throw new ConstellationIsStillUsedException();
            }
            constellationRepository.deleteById(id);
        }

    }

}
