package wisematches.server.web.controllers.gameplaying;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import wisematches.server.personality.Personality;
import wisematches.server.personality.player.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractPlayerController {
	protected AbstractPlayerController() {
	}

	@ModelAttribute("player")
	public Player getPlayer() {
		return (Player) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@ModelAttribute("personality")
	public Personality getPersonality() {
		return (Personality) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
