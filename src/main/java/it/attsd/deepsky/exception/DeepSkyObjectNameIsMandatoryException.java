package it.attsd.deepsky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Deep-sky object name is mandatory")
public class DeepSkyObjectNameIsMandatoryException extends Exception {
//	public DeepSkyObjectNameIsMandatoryException() {
//		super("Deep-sky object name is mandatory");
//	}
}
