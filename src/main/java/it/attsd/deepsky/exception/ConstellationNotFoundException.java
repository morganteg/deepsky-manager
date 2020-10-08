package it.attsd.deepsky.exception;

@SuppressWarnings("serial")
public class ConstellationNotFoundException extends Exception {

	public ConstellationNotFoundException() {
		super("Constellation not found");
	}
	
}
