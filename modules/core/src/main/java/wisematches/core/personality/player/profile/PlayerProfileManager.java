package wisematches.core.personality.player.profile;

import wisematches.core.personality.player.account.Account;

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
	 * @param account the personality who's profile should be returned.
	 * @return the profile for specified personality or {@code null} if personality doesn't have a profile.
	 */
	PlayerProfile getPlayerProfile(Account account);

	/**
	 * Updates the player profile.
	 *
	 * @param account       the personality who's profile should be updated
	 * @param playerProfile the profile object with new information about the player.
	 */
	void updateProfile(Account account, PlayerProfile playerProfile);
}
