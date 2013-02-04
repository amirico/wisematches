package wisematches.server.services.blacklist;

import wisematches.core.Player;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BlacklistManager {
	void addBlacklistListener(BlacklistListener l);

	void removeBlacklistListener(BlacklistListener l);

	/**
	 * Adds a {@code whom} personality to the {@code person} blacklist
	 *
	 * @param person  the person who wants add another one to black list
	 * @param whom    the person who should be added to blacklist.
	 * @param comment the ignore comment
	 */
	void addPlayer(Player person, Player whom, String comment);

	/**
	 * Removes a {@code whom} personality from the {@code person} blacklist
	 *
	 * @param person the person who wants remove another one from black list
	 * @param whom   the person who should be removed to blacklist.
	 */
	void removePlayer(Player person, Player whom);

	/**
	 * Checks is {@code whom} person in a black list of the {@code peson} personality.
	 *
	 * @param person the person who checks a blacklist
	 * @param whom   the person who is checked.
	 * @return {@code true} if {@code whom} personality in blacklist of {@code person}; {@code false} - otherwise.
	 */
	boolean isBlacklisted(Player person, Player whom);

	/**
	 * Checks is {@code whom} person in blacklist of the {@code peson} personality.
	 *
	 * @param person the person who checks a blacklist
	 * @param whom   the person who is checked.
	 * @throws BlacklistedException if {@code whom} person in blacklist of specified {@code person}.
	 */
	void checkBlacklist(Player person, Player whom) throws BlacklistedException;

	/**
	 * Returns collection of all players in a blacklist.
	 *
	 * @param person the person who's blacklist should be returned.
	 * @return the collection of
	 */
	Collection<BlacklistRecord> getBlacklist(Player person);
}
