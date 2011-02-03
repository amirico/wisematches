package wisematches.server.web.security.guest;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMGuestAuthenticationProvider implements AuthenticationProvider {
	public WMGuestAuthenticationProvider() {
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
		return WMGuestAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
