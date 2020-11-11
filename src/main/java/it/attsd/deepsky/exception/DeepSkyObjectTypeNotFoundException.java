package it.attsd.deepsky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "deepSkyObjectType not found")
public class DeepSkyObjectTypeNotFoundException extends Exception {

	public DeepSkyObjectTypeNotFoundException() {
		super("DeepSkyObjectType not found");
	}
	
}
