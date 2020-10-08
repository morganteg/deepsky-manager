package it.attsd.deepsky.exception;

@SuppressWarnings("serial")
public class DeepSkyObjectTypeNotFoundException extends Exception {

	public DeepSkyObjectTypeNotFoundException() {
		super("DeepSkyObjectType not found");
	}
	
}
