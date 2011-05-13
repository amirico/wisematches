package wisematches.personality.player;

import wisematches.personality.Personality;

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
}