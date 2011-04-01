package wisematches.server.security;

import wisematches.server.personality.account.Account;

/**
 * Simple interface for interact with security over web application.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerSecurityService {
	/**
	 * Encode password for specified player.
	 *
	 * @param account  the player who's password should be encoded.
	 * @param password the password to be encoded.
	 * @return the encoded password.
	 */
	public String encodePlayerPassword(Account account, String password);
}
