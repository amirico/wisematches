package wisematches.server.web.security.captcha;

import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface CaptchaService {
	String createCaptchaScript(String language);

	void validateCaptcha(HttpServletRequest request, HttpServletResponse response, Errors errors);
}