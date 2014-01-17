package wisematches.server.web.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import wisematches.server.web.security.PlayerDetails;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerAuthentication extends AbstractAuthenticationToken {
	private PlayerDetails personality;

	public PlayerAuthentication(PlayerDetails personality) {
		super(personality.getAuthorities());
		setAuthenticated(true);
		this.personality = personality;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public PlayerDetails getPrincipal() {
		return personality;
	}
}
