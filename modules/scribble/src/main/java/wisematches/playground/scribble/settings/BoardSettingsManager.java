package wisematches.playground.scribble.settings;

import wisematches.core.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardSettingsManager {
	/**
	 * Returns notification mask for specified personality.
	 *
	 * @param player the personality who's mask should be returned.
	 * @return the notification's mask.
	 */
	BoardSettings getScribbleSettings(Player player);

	/**
	 * Updates notifications mask for specified player.
	 *
	 * @param personality the player who's notifications mask should be updated.
	 * @param settings    the new settings for specified personality
	 */
	void setScribbleSettings(Player player, BoardSettings settings);
}
