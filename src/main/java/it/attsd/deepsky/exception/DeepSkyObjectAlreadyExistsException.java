package it.attsd.deepsky.exception;

@SuppressWarnings("serial")
public class DeepSkyObjectAlreadyExistsException extends Exception {

	public DeepSkyObjectAlreadyExistsException() {
		super("DeepSkyObject already exists");
	}
	
}
