package wisematches.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.security.userdetails.PersonalityDetails;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PersonalityContext {
	private PersonalityContext() {
	}

	public static Player getPlayer() {
		final Personality personality = getPersonality();
		if (personality instanceof Player) {
			return (Player) personality;
		}
		return null;
	}

	public static Personality getPersonality() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof PersonalityDetails) {
			return ((PersonalityDetails) authentication.getPrincipal()).getPersonality();
		}
		return null;
	}
}
