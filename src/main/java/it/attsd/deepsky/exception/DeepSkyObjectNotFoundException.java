package it.attsd.deepsky.exception;

@SuppressWarnings("serial")
public class DeepSkyObjectNotFoundException extends Exception {

	public DeepSkyObjectNotFoundException() {
		super("DeepSkyObject not found");
	}
	
}
