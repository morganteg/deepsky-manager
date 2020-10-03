package it.attsd.deepsky.exception;

@SuppressWarnings("serial")
public class ConstellationAlreadyExistsException extends Exception {

	public ConstellationAlreadyExistsException() {
		super("Constellation already exists");
	}
	
}
