package wisematches.server.security;

import org.springframework.security.core.context.SecurityContextHolder;
import wisematches.server.personality.Personality;
import wisematches.server.personality.player.Player;
import wisematches.server.security.impl.WMUserDetails;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class WMSecurityContext {
	private WMSecurityContext() {
	}

	public static Player getPlayer() {
		WMUserDetails details = (WMUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
		return details.getPlayer();
	}

	public static Personality getPersonality() {
		return (Personality) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}
}
