package it.attsd.deepsky.exception;

@SuppressWarnings("serial")
public class DeepSkyObjectTypeAlreadyExistsException extends Exception {

	public DeepSkyObjectTypeAlreadyExistsException() {
		super("DeepSkyObjectType already exists");
	}
	
}
