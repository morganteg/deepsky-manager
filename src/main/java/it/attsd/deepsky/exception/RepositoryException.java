package it.attsd.deepsky.exception;

@SuppressWarnings("serial")
public class RepositoryException extends Exception {

	public RepositoryException() {
		super();
	}
	
	public RepositoryException(Exception e) {
		super(e);
	}
	
	public RepositoryException(String message) {
		super(message);
	}
	
}
