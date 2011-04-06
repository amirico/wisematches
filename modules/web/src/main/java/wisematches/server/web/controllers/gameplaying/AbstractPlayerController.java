package wisematches.server.web.controllers.gameplaying;

import wisematches.server.personality.Personality;
import wisematches.server.personality.player.Player;
import wisematches.server.security.WMSecurityContext;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractPlayerController {
	protected AbstractPlayerController() {
	}

	@ModelAttribute("player")
	public Player getPlayer() {
		return WMSecurityContext.getPlayer();
	}

	@ModelAttribute("personality")
	public Personality getPersonality() {
		return WMSecurityContext.getPersonality();
	}
}
