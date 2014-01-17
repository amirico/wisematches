package wisematches.server.web.security.authentication;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import wisematches.server.web.security.PlayerDetails;
import wisematches.server.web.security.PlayerDetailsService;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class PlayerAuthenticationProvider implements AuthenticationProvider, InitializingBean {
	protected PlayerDetailsService playerDetailsService;
	private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

	protected PlayerAuthenticationProvider() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (playerDetailsService == null) {
			throw new IllegalStateException("PersonalityManager is not set");
		}
	}

	@Override
	public final PlayerAuthentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}

		final PlayerDetails details = loadValidPersonalityDetails(authentication);
		userDetailsChecker.check(details);

		final PlayerAuthentication res = new PlayerAuthentication(details);
		res.setDetails(authentication.getDetails());
		return res;
	}

	protected abstract PlayerDetails loadValidPersonalityDetails(Authentication authentication);

	public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
	}

	public void setPlayerDetailsService(PlayerDetailsService playerDetailsService) {
		this.playerDetailsService = playerDetailsService;
	}
}
