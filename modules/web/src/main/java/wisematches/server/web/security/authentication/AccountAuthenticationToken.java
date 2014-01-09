package wisematches.server.web.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import wisematches.core.personality.player.account.Account;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountAuthenticationToken extends AbstractAuthenticationToken {
	private final Account account;

	public AccountAuthenticationToken(Account account) {
		super(null);
		this.account = account;
	}

	@Override
	public String getName() {
		return account.getEmail();
	}

	@Override
	public Account getPrincipal() {
		return account;
	}

	@Override
	public Object getCredentials() {
		return null;
	}
}