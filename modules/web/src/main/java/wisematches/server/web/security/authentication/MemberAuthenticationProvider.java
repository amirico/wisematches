package wisematches.server.web.security.authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wisematches.server.web.security.PlayerDetails;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MemberAuthenticationProvider extends PlayerAuthenticationProvider {
	public MemberAuthenticationProvider() {
	}

	@Override
	protected PlayerDetails loadValidPersonalityDetails(Authentication authentication) {
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			return getUsernamePasswordDetails((UsernamePasswordAuthenticationToken) authentication);
		} else if (authentication instanceof AccountAuthenticationToken) {
			return getMemberDetails((AccountAuthenticationToken) authentication);
		}
		throw new InternalAuthenticationServiceException("Unsupported authentication: " + authentication);
	}

	private PlayerDetails getMemberDetails(AccountAuthenticationToken token) {
		return playerDetailsService.loadUserByAccount(token.getPrincipal());
	}

	private PlayerDetails getUsernamePasswordDetails(UsernamePasswordAuthenticationToken token) {
		final String username = (String) token.getPrincipal();
		final String password = (String) token.getCredentials();
		if (password == null) {
			throw new BadCredentialsException("No password provided");
		}

		final PlayerDetails details = playerDetailsService.loadUserByUsername(username);
		if (details == null) {
			throw new UsernameNotFoundException("User is unknown: " + username);
		}

		if (!playerDetailsService.isPasswordValid(details, password)) {
			throw new BadCredentialsException("Passwords mismatch");
		}
		return details;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication) ||
				AccountAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
