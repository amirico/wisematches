package wisematches.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import wisematches.core.Player;
import wisematches.core.security.userdetails.PlayerDetails;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PersonalityContext {
	private PersonalityContext() {
	}

	public static Player getPlayer() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof PlayerDetails) {
			return ((PlayerDetails) authentication.getPrincipal()).getPlayer();
		}
		return null;
	}
}
