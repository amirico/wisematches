package wisematches.playground.scribble.settings;

import wisematches.core.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardSettingsManager {
	/**
	 * Returns notification mask for specified personality.
	 *
	 * @param personality the personality who's mask should be returned.
	 * @return the notification's mask.
	 */
	BoardSettings getScribbleSettings(Personality personality);

	/**
	 * Updates notifications mask for specified player.
	 *
	 * @param personality the player who's notifications mask should be updated.
	 * @param settings    the new settings for specified personality
	 */
	void setScribbleSettings(Personality personality, BoardSettings settings);
}
