package it.attsd.deepsky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "deepSkyObject not found")
public class DeepSkyObjectNotFoundException extends Exception {

	public DeepSkyObjectNotFoundException() {
		super("DeepSkyObject not found");
	}
	
}
