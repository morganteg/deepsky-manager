package it.attsd.deepsky.exceptions;

public class ConstellationIsStillUsedException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = -9078034581857181868L;

	public ConstellationIsStillUsedException() {
        super("The Constellation is used by a DeepSkyObject");
    }
}
