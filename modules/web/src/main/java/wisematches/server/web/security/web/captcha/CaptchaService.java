package wisematches.server.web.security.web.captcha;

import org.springframework.validation.Errors;
import org.springframework.web.context.request.NativeWebRequest;
import wisematches.playground.GameMessageSource;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface CaptchaService {
	void validateCaptcha(NativeWebRequest webRequest, Errors errors);

	String createCaptchaScript(GameMessageSource messageSource, Locale language);
}