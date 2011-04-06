package wisematches.server.web.exceptions;

import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
public class SecurityExceptionHandler {
	public SecurityExceptionHandler() {
	}

	@ExceptionHandler(CookieTheftException.class)
	public String cookieTheftException(CookieTheftException ex) {
//		Authentication authentication = ex.getAuthentication();
		return "forward:/account/loginAuth.html?error=insufficient";
	}
}
