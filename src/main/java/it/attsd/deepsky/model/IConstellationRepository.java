package it.attsd.deepsky.model;

import java.util.List;

import it.attsd.deepsky.entity.Constellation;

public interface IConstellationRepository {

	public List<Constellation> findAll();

	public Constellation findById(long id);

}
