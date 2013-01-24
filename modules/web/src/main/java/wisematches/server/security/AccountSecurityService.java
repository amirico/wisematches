package wisematches.server.security;

import wisematches.core.personality.player.account.Account;

/**
 * Simple interface for interact with security over web application.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AccountSecurityService {
	/**
	 * Re-authenticate specified account in current session and update security context.
	 *
	 * @param account  the account to be authenticated again.
	 * @param password the new password if it was changed or {@code null} or old one must be used.
	 */
	public void authenticatePlayer(Account account, String password);

	/**
	 * Encode password for specified player.
	 *
	 * @param account  the player who's password should be encoded.
	 * @param password the password to be encoded.
	 * @return the encoded password.
	 */
	public String encodePlayerPassword(Account account, String password);
}
