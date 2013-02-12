package wisematches.server.web.servlet;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import wisematches.core.security.PersonalityContext;
import wisematches.server.web.servlet.mvc.UnknownEntityException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
@Controller
@RequestMapping("/info/error")
public class WMExceptionResolver extends AnnotationMethodHandlerExceptionResolver {
	public WMExceptionResolver() {
	}


	@RequestMapping(value = "")
	public ModelAndView processException() {
		return processException("???", null);
	}

	@RequestMapping(value = "", params = "c=400")
	@ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, TypeMismatchException.class})
	public ModelAndView processBadRequest() {
		return processException("400", null);
	}

	@RequestMapping(value = "", params = "c=404")
	@ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	public ModelAndView processPageNotFound(HttpServletRequest request) {
		return processException("404", null, request.getRequestURI());
	}

	@RequestMapping(value = "", params = "c=500")
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
		return "forward:/account/loginAuth?error=insufficient";
	}

	private ModelAndView processException(String errorCode, Exception exception, Object... arguments) {
		final ModelAndView res = new ModelAndView("/content/templates/errors");
		res.addObject("errorCode", errorCode);
		res.addObject("errorArguments", arguments);
		res.addObject("errorException", exception);
		res.addObject("principal", PersonalityContext.getPlayer());
		res.addObject("personality", PersonalityContext.getPersonality());
		res.addObject("title", "title.playboard");
		return res;
	}

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		return super.doResolveException(request, response, this, ex);
	}
}
