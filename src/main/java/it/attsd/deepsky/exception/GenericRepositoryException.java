package it.attsd.deepsky.exception;

@SuppressWarnings("serial")
public class GenericRepositoryException extends Exception {

	public GenericRepositoryException(Exception e) {
		super(e);
	}
	
	public GenericRepositoryException(String message) {
		super(message);
	}
	
}
