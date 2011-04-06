package wisematches.server.web.controllers.errors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import wisematches.server.personality.player.Player;
import wisematches.server.security.WMSecurityContext;
import wisematches.server.web.controllers.AbstractInfoController;

import javax.servlet.ServletException;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
public class DefaultExceptionHandler extends AbstractInfoController {
	private static final Log log = LogFactory.getLog("wisematches.server.web.exceptions");

	public DefaultExceptionHandler() {
	}

	@RequestMapping("/error/404")
	@ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	public String processPageNotFound() {
		return "/content/errors/notFound";
	}

	@ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class, TypeMismatchException.class})
	public String processBadRequest() {
		return "/content/errors/badRequest";
	}

	@ExceptionHandler(Exception.class)
	public String processUnhandledException(Exception ex) {
		log.error("Unhandled exception received", ex);
		return "/content/errors/default";
	}

	@ModelAttribute("player")
	public Player getPlayer() {
		return WMSecurityContext.getPlayer();
	}
}
