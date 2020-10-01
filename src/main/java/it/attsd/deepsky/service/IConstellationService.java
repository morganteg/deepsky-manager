package it.attsd.deepsky.service;

import java.util.List;

import it.attsd.deepsky.entity.Constellation;

@Deprecated
public interface IConstellationService {
	
	public List<Constellation> findAll();
	
	public Constellation findById(long id);
	
	public void save(Constellation constellation);
	
	public void update(Constellation constellation);
	
	public void delete(long id);

}
