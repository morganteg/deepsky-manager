package it.attsd.deepsky.exceptions;

public class ConstellationIsStillUsedException extends Exception {
    public ConstellationIsStillUsedException() {
        super("The Constellation is used by a DeepSkyObject");
    }
}
