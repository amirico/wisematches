package wisematches.server.web.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.core.security.PersonalityContext;
import wisematches.playground.GameMessageSource;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.sdo.ServiceResponseFactory;
import wisematches.server.web.servlet.view.StaticContentGenerator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@ControllerAdvice
@SuppressWarnings("unchecked")
public abstract class WisematchesController {
	private final String title;

	protected GameMessageSource messageSource;
	protected ServiceResponseFactory responseFactory;
	protected PlayerStateManager playerStateManager;
	protected PersonalityManager personalityManager;
	protected StaticContentGenerator staticContentGenerator;

	protected WisematchesController() {
		this("title.playboard");
	}

	protected WisematchesController(String title) {
		this.title = title;
	}

	@ModelAttribute("title")
	public String getTitle() {
		return title;
	}

	@ModelAttribute("player")
	public Player getPlayer() {
		return PersonalityContext.getPlayer();
	}

	protected boolean hasRole(String role) {
		return PersonalityContext.hasRole(role);
	}

	protected <P extends Player> P getPlayer(Class<P> type) {
		final Player player = getPlayer();
		if (player == null) {
			throw new AccessDeniedException("unregistered");
		}
		if (type.isAssignableFrom(player.getClass())) {
			throw new AccessDeniedException("unregistered");
		}
		return (P) player;
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