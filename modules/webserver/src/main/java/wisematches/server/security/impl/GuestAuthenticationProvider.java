package wisematches.server.security.impl;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GuestAuthenticationProvider implements AuthenticationProvider {
	public GuestAuthenticationProvider() {
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}
		return authentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return GuestAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
