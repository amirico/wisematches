package wisematches.core.security.authentication;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import wisematches.core.security.userdetails.PersonalityDetails;
import wisematches.core.security.userdetails.PersonalityDetailsService;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class PersonalityAuthenticationProvider implements AuthenticationProvider, InitializingBean {
	private PersonalityDetailsService personalityDetailsService;
	private UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();

	protected PersonalityAuthenticationProvider() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (personalityDetailsService == null) {
			throw new IllegalStateException("PersonalityManager is not set");
		}
	}

	@Override
	public final PersonalityAuthentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}

		final PersonalityDetails details = loadValidPersonalityDetails(authentication);
		userDetailsChecker.check(details);

		final PersonalityAuthentication res = new PersonalityAuthentication(details);
		res.setDetails(authentication.getDetails());
		return res;
	}

	protected abstract PersonalityDetails loadValidPersonalityDetails(Authentication authentication);

	public PersonalityDetailsService getPersonalityDetailsService() {
		return personalityDetailsService;
	}

	public void setUserDetailsChecker(UserDetailsChecker userDetailsChecker) {
		this.userDetailsChecker = userDetailsChecker;
	}

	public void setPersonalityDetailsService(PersonalityDetailsService personalityDetailsService) {
		this.personalityDetailsService = personalityDetailsService;
	}
}
