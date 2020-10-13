package it.attsd.deepsky.dto;

import java.util.ArrayList;
import java.util.List;

import it.attsd.deepsky.entity.Constellation;

public class ConstellationList {
    private List<Constellation> constellations;
 
    public ConstellationList() {
    	constellations = new ArrayList<Constellation>();
    }

	public List<Constellation> getConstellations() {
		return constellations;
	}

	public void setConstellations(List<Constellation> constellations) {
		this.constellations = constellations;
	}
    
}
