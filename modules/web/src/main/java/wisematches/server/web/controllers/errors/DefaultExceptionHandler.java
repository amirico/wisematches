package wisematches.server.web.controllers.errors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import wisematches.server.personality.player.Player;
import wisematches.server.security.WMSecurityContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
public class DefaultExceptionHandler {
	private static final Log log = LogFactory.getLog("wisematches.server.web.exceptions");

	public DefaultExceptionHandler() {
	}

	@RequestMapping("/error/400.html")
	@ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, TypeMismatchException.class})
	public String processBadRequest(Model model) {
		return processException("400", null, model);
	}

	@RequestMapping("/error/404.html")
	@ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	public String processPageNotFound(Model model, HttpServletRequest request) {
		return processException("404", null, model, request.getRequestURI());
	}

	@RequestMapping("/error/500.html")
	@ExceptionHandler(Exception.class)
	public String processUnhandledException(Model model, Exception exception) {
		return processException("500", exception, model);
	}

	private String processException(String errorCode, Exception exception, Model model, Object... arguments) {
		if (exception != null) {
			log.error("Error notification received", exception);
		}
		model.addAttribute("errorCode", errorCode);
		model.addAttribute("errorArguments", arguments);
		model.addAttribute("errorException", model);
		return "/content/errors/layout";
	}

	@ModelAttribute("player")
	public Player getPlayer() {
		return WMSecurityContext.getPlayer();
	}
}
