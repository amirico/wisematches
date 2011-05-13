package wisematches.server.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import wisematches.server.security.WMSecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMExceptionResolver extends AnnotationMethodHandlerExceptionResolver {
	private static final Log log = LogFactory.getLog("wisematches.server.web.exceptions");

	public WMExceptionResolver() {
	}

	@RequestMapping("/error/400.html")
	@ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, TypeMismatchException.class})
	public ModelAndView processBadRequest() {
		return processException("400", null);
	}

	@RequestMapping("/error/404.html")
	@ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	public ModelAndView processPageNotFound(HttpServletRequest request) {
		return processException("404", null, request.getRequestURI());
	}

	@RequestMapping("/error/500.html")
	@ExceptionHandler(Exception.class)
	public ModelAndView processUnhandledException(Exception exception) {
		return processException("500", exception);
	}

	@ExceptionHandler(UnknownEntityException.class)
	public ModelAndView processUnknownEntity(UnknownEntityException exception) {
		return processException("unknown." + exception.getEntityType(), null, exception.getEntityId());
	}

	@ExceptionHandler(CookieTheftException.class)
	public String cookieTheftException(CookieTheftException ex) {
		return "forward:/account/loginAuth.html?error=insufficient";
	}

	private ModelAndView processException(String errorCode, Exception exception, Object... arguments) {
		if (exception != null) {
			log.error("Error notification received", exception);
		}
		final ModelAndView res = new ModelAndView("/content/errors/layout");
		res.addObject("errorCode", errorCode);
		res.addObject("errorArguments", arguments);
		res.addObject("errorException", exception);
		res.addObject("player", WMSecurityContext.getPlayer());
		res.addObject("personality", WMSecurityContext.getPersonality());
		res.addObject("headerTitle", "title.playboard");
		return res;
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		return super.doResolveException(request, response, this, ex);
	}
}
