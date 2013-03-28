package wisematches.server.web.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.core.security.PersonalityContext;
import wisematches.playground.GameMessageSource;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.sdo.ServiceResponseFactory;
import wisematches.server.web.servlet.view.StaticContentGenerator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@ControllerAdvice
public abstract class WisematchesController {
	protected GameMessageSource messageSource;
	protected ServiceResponseFactory responseFactory;
	protected PlayerStateManager playerStateManager;
	protected PersonalityManager personalityManager;
	protected StaticContentGenerator staticContentGenerator;

	protected WisematchesController() {
	}

	@ModelAttribute("title")
	public String getTitle(HttpServletRequest request) {
		final Object title = request.getAttribute("title");
		if (title != null) {
			return String.valueOf(title);
		}
		final String uri = request.getServletPath() + request.getPathInfo();
		if (uri.length() <= 1) {
			return "title.default";
		}
		return "title." + uri.replaceAll("/", ".").substring(1);
	}

	@ModelAttribute("principal")
	public Player getPrincipal() {
		return PersonalityContext.getPrincipal();
	}

	protected void addTitleExtension(String value, Model model) {
		model.addAttribute("titleExtension", value);
	}

	@SuppressWarnings("unchecked")
	protected <P extends Player> P getPrincipal(Class<P> type) {
		final Player principal = getPrincipal();
		if (principal == null) {
			throw new AccessDeniedException("unregistered");
		}
		if (!type.isAssignableFrom(principal.getClass())) {
			throw new AccessDeniedException("unregistered");
		}
		return (P) principal;
	}

	/*
		@ModelAttribute("requestQueryString")
		public String getRequestQueryString() {
			final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			return request.getQueryString();
		}
	*/
	@Autowired
	public void setMessageSource(GameMessageSource messageSource) {
		this.messageSource = messageSource;
		this.responseFactory = new ServiceResponseFactory(messageSource);
	}

	@Autowired
	public void setPersonalityManager(PersonalityManager personalityManager) {
		this.personalityManager = personalityManager;
	}

	@Autowired
	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		this.playerStateManager = playerStateManager;
	}

	@Autowired
	public void setStaticContentGenerator(StaticContentGenerator staticContentGenerator) {
		this.staticContentGenerator = staticContentGenerator;
	}
}