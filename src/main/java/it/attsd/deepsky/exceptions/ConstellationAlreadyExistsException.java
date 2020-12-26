package it.attsd.deepsky.exceptions;

public class ConstellationAlreadyExistsException extends Exception {
    public ConstellationAlreadyExistsException() {
        super("A Constellation with the same name already exists");
    }
}
