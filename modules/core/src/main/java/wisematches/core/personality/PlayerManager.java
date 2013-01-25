package wisematches.core.personality;

import wisematches.core.Player;

/**
 * The {@code PersonalityManager} provides ability to get players and machineries by id.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerManager {
	void addPlayerListener(PlayerListener listener);

	void removePlayerListener(PlayerListener listener);

	/**
	 * Returns player by it's id.
	 *
	 * @param id the player's id
	 * @return player associated with specified id or {@code null} if player is unknown.
	 */
	Player getPlayer(Long id);
}