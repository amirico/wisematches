package wisematches.server.web.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import wisematches.core.Personality;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.core.security.PersonalityContext;
import wisematches.playground.GameMessageSource;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.view.StaticContentGenerator;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@ControllerAdvice
public abstract class WisematchesController {
	private final String title;

	protected GameMessageSource messageSource;
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

	@ModelAttribute("personality")
	public Personality getPersonality() {
		return PersonalityContext.getPersonality();
	}

	@ModelAttribute("requestQueryString")
	public String getRequestQueryString() {
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return request.getQueryString();
	}

	protected Player getPlayer() {
		final Personality personality = getPersonality();
		if (personality == null) {
			return null;
		}
		if (!(personality instanceof Player)) {
			throw new AccessDeniedException("Incorrect personality type where Player is expected: " + personality.getType());
		}
		return (Player) personality;
	}

	protected boolean isPlayerAuthorized() {
		return getPersonality() instanceof Player;
	}

	@Autowired
	public void setMessageSource(GameMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Autowired
	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		this.playerStateManager = playerStateManager;
	}

	@Autowired
	public void setPersonalityManager(PersonalityManager personalityManager) {
		this.personalityManager = personalityManager;
	}

	@Autowired
	public void setStaticContentGenerator(StaticContentGenerator staticContentGenerator) {
		this.staticContentGenerator = staticContentGenerator;
	}
}