package it.attsd.deepsky.exceptions;

public class ConstellationAlreadyExistsException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8440529590750284203L;

	public ConstellationAlreadyExistsException() {
        super("A Constellation with the same name already exists");
    }
}
