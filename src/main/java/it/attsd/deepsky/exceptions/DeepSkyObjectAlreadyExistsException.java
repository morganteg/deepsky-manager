package it.attsd.deepsky.exceptions;

public class DeepSkyObjectAlreadyExistsException extends Exception {
    public DeepSkyObjectAlreadyExistsException() {
        super("A DeepSkyObject with the same name already exists");
    }
}
