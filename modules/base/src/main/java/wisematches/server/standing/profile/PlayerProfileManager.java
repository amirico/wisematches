package wisematches.server.standing.profile;

import wisematches.server.personality.Personality;

/**
 * {@code PlayerProfileManager} provides access to {@code PlayerProfile} object with details about a player.
 * <p/>
 * This manager doesn't have any create or remove methods and implementation has to work with
 * {@code AccountManager} to get required information about an account state.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerProfileManager {
	void addPlayerProfileListener(PlayerProfileListener l);

	void removePlayerProfileListener(PlayerProfileListener l);

	/**
	 * Returns profile for specified personality.
	 *
	 * @param personality the personality who's profile should be returned.
	 * @return the profile for specified personality or {@code null} if personality doesn't have a profile.
	 */
	PlayerProfile getPlayerProfile(Personality personality);

	/**
	 * Updates the player profile.
	 *
	 * @param playerProfile the profile object with new information about the player.
	 */
	void updateProfile(PlayerProfile playerProfile);
}
