package wisematches.personality.player;

import wisematches.server.personality.Personality;

/**
 * Players manager interface. This
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerManager {
	/**
	 * Returns player by it's id.
	 *
	 * @param playerId the player's id.
	 * @return the player by it's id or <code>null</code> if player is unknown.
	 */
	Player getPlayer(long playerId);

	/**
	 * Returns player by it's id.
	 *
	 * @param personality the player's personality.
	 * @return the player by it's id or <code>null</code> if player is unknown.
	 */
	Player getPlayer(Personality personality);

	/**
	 * Searches a player by specified email.
	 *
	 * @param email the email to be searched
	 * @return the found player or {@code null} if no player with specified email.
	 */
	Player findByEmail(String email);

	/**
	 * Searches a player by specified username.
	 *
	 * @param username the username to be searched
	 * @return the found player or {@code null} if no player with specified username.
	 */
	Player findByUsername(String username);
}