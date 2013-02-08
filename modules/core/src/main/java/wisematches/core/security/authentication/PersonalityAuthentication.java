package wisematches.core.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import wisematches.core.security.userdetails.PersonalityDetails;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PersonalityAuthentication extends AbstractAuthenticationToken {
	private PersonalityDetails personality;

	public PersonalityAuthentication(PersonalityDetails personality) {
		super(personality.getAuthorities());
		setAuthenticated(true);
		this.personality = personality;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public PersonalityDetails getPrincipal() {
		return personality;
	}
}
