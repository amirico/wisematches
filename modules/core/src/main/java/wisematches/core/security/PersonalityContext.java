package wisematches.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import wisematches.core.Personality;
import wisematches.core.security.userdetails.PersonalityDetails;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class PersonalityContext {
	private PersonalityContext() {
	}

	public static Personality getPersonality() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof PersonalityDetails) {
			return ((PersonalityDetails) authentication.getPrincipal()).getPersonality();
		}
		return null;
	}
}
