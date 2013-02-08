package wisematches.core.security.authentication;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import wisematches.core.security.userdetails.PersonalityDetails;
import wisematches.core.security.userdetails.PersonalityDetailsService;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PlayerAuthenticationProvider extends PersonalityAuthenticationProvider {
	public PlayerAuthenticationProvider() {
	}

	@Override
	protected PersonalityDetails loadValidPersonalityDetails(Authentication authentication) {
		final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		final String username = (String) token.getPrincipal();
		final String password = (String) token.getCredentials();
		if (password == null) {
			throw new BadCredentialsException("No password provided");
		}

		final PersonalityDetailsService detailsService = getPersonalityDetailsService();
		final PersonalityDetails details = detailsService.loadPlayerByEmail(username);
		if (details == null) {
			throw new UsernameNotFoundException("User is unknown: " + username);
		}

		if (!detailsService.isPasswordValid(details, password)) {
			throw new BadCredentialsException("Passwords mismatch");
		}
		return details;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
