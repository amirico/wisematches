package wisematches.server.web.controllers.info;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class InformationUnavailableException extends Exception {
	public InformationUnavailableException(String pageName, String plain) {
	}
}
