package wisematches.server.web.security.social;

import org.springframework.social.UserIdSource;
import wisematches.core.Player;
import wisematches.server.web.security.context.PersonalityContext;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PersonalityUserIdSource implements UserIdSource {
	public PersonalityUserIdSource() {
	}

	@Override
	public String getUserId() {
		final Player principal = PersonalityContext.getPrincipal();
		if (principal == null) {
			return null;
		}
		return String.valueOf(principal.getId());
	}
}
