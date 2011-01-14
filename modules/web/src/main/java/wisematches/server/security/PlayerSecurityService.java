package wisematches.server.security;

import wisematches.server.player.Player;

/**
 * Simple interface for interact with security over web application.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerSecurityService {
	/**
	 * Authenticate specified player in active session.
	 *
	 * @param player   the player to be authenticated.
	 * @param password the plain the player's password for authentication.
	 */
	public void authenticatePlayer(Player player, String password);

	/**
	 * Encode password for specified player.
	 *
	 * @param player   the player who's password should be encoded.
	 * @param password the password to be encoded.
	 * @return the encoded password.
	 */
	public String encodePlayerPassword(Player player, String password);
}
