package it.attsd.deepsky.exception;

@SuppressWarnings("serial")
public class DeepSkyObjectEmptyAttributeException extends Exception {

	public DeepSkyObjectEmptyAttributeException(String attributeName) {
		super(String.format("DeepSkyObject attribute \"%s\" is empty", attributeName));
	}
	
}
