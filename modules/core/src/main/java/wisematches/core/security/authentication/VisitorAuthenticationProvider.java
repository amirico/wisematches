package wisematches.core.security.authentication;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import wisematches.core.Language;
import wisematches.core.security.userdetails.PlayerDetails;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class VisitorAuthenticationProvider extends PlayerAuthenticationProvider {
	private int visitorKeyHast;

	public VisitorAuthenticationProvider() {
	}

	@Override
	protected PlayerDetails loadValidPersonalityDetails(Authentication authentication) {
		final AnonymousAuthenticationToken token = (AnonymousAuthenticationToken) authentication;
		if (token.getKeyHash() != visitorKeyHast) {
			return null;
		}

		final Object principal = token.getPrincipal();
		if (!(principal instanceof Language)) {
			throw new BadCredentialsException("Principal must be language code");
		}

		final Language lang = (Language) principal;
		return getPersonalityDetailsService().loadVisitorByLanguage(lang);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AnonymousAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public void setVisitorKey(String visitorKey) {
		this.visitorKeyHast = visitorKey.hashCode();
	}
}
