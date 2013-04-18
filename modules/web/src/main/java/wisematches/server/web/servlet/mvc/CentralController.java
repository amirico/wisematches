package wisematches.server.web.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import wisematches.server.web.servlet.mvc.playground.player.profile.ProfileController;
import wisematches.server.web.servlet.mvc.playground.player.profile.form.PlayerProfileForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@ControllerAdvice
public class CentralController extends WisematchesController {
	private ProfileController playerProfileController;

	public CentralController() {
	}

	@RequestMapping(value = {"/", "/index"})
	public final String mainPage() {
		if (getPrincipal() != null) {
			return "redirect:/playground/scribble/active";
		}
		return "forward:/account/login";
	}

	@RequestMapping("/playground/welcome")
	public String welcomePage(Model model, @ModelAttribute("form") PlayerProfileForm form, Locale locale) {
		playerProfileController.editProfilePage(model, form, locale);
		return "/content/playground/welcome";
	}

	@RequestMapping(value = "/info/error")
	public ModelAndView processException(HttpServletRequest request, HttpServletResponse response) {
		return processException(String.valueOf(response.getStatus()), null, request, request.getRequestURI());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView processAccessException(Exception exception, HttpServletRequest request) {
		return processException("access", exception, request);
	}

	@ExceptionHandler(UnknownEntityException.class)
	public ModelAndView processUnknownEntity(UnknownEntityException exception, HttpServletRequest request) {
		return processException("unknown." + exception.getEntityType(), null, request, exception.getEntityId());
	}

	@ExceptionHandler(CookieTheftException.class)
	public String cookieTheftException(CookieTheftException ex) {
		return "forward:/account/loginAuth?error=insufficient";
	}

	private ModelAndView processException(String errorCode, Exception exception, HttpServletRequest request, Object... arguments) {
		final ModelAndView res = new ModelAndView("/content/errors");
		res.addObject("title", getTitle(request));
		res.addObject("principal", getPrincipal());

		res.addObject("errorCode", errorCode);
		res.addObject("errorArguments", arguments);
		res.addObject("errorException", exception);
		return res;
	}

	@Autowired
	public void setPlayerProfileController(ProfileController playerProfileController) {
		this.playerProfileController = playerProfileController;
	}
}
