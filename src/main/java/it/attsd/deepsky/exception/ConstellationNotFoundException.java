package it.attsd.deepsky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "constellation not found")
public class ConstellationNotFoundException extends Exception {
	
	public ConstellationNotFoundException() {
		super("Constellation not found");
	}
	
}
