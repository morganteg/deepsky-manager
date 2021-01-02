package it.attsd.deepsky.exceptions;

public class DeepSkyObjectAlreadyExistsException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -8955431040923103521L;

	public DeepSkyObjectAlreadyExistsException() {
        super("A DeepSkyObject with the same name already exists");
    }
}
