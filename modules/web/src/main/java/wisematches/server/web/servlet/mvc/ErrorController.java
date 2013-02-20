package wisematches.server.web.servlet.mvc;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import wisematches.core.security.PersonalityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@ControllerAdvice
@RequestMapping("/info/error")
public class ErrorController {
	public ErrorController() {
	}

	@RequestMapping(value = "")
	public ModelAndView processException(HttpServletRequest request, HttpServletResponse response) {
		return processException(String.valueOf(response.getStatus()), null, request.getRequestURI());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView processAccessException(Exception exception) {
		return processException("access", exception);
	}

	@ExceptionHandler(UnknownEntityException.class)
	public ModelAndView processUnknownEntity(UnknownEntityException exception) {
		return processException("unknown." + exception.getEntityType(), null, exception.getEntityId());
	}

	@ExceptionHandler(CookieTheftException.class)
	public String cookieTheftException(CookieTheftException ex) {
		return "forward:/account/loginAuth?error=insufficient";
	}

	private ModelAndView processException(String errorCode, Exception exception, Object... arguments) {
		final ModelAndView res = new ModelAndView("/content/errors");
		res.addObject("title", "title.playboard");
		res.addObject("errorCode", errorCode);
		res.addObject("errorArguments", arguments);
		res.addObject("errorException", exception);
		res.addObject("personality", PersonalityContext.getPlayer());
		return res;
	}
}
