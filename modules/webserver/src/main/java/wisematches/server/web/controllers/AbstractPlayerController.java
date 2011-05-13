package wisematches.server.web.controllers;

import org.springframework.web.bind.annotation.ModelAttribute;
import wisematches.personality.player.Player;
import wisematches.server.personality.Personality;
import wisematches.server.security.WMSecurityContext;

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

	@ModelAttribute("headerTitle")
	public String getHeaderTitle() {
		return "title.playboard";
	}
}
