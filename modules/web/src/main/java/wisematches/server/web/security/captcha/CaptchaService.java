package wisematches.server.web.security.captcha;

import org.springframework.validation.Errors;
import wisematches.playground.GameMessageSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface CaptchaService {
	String createCaptchaScript(GameMessageSource messageSource, Locale language);

	void validateCaptcha(HttpServletRequest request, HttpServletResponse response, Errors errors);
}