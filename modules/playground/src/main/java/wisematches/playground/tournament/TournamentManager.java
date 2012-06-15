package wisematches.playground.tournament;

import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;

/**
 * {@code TournamentManager} provides access to active tournaments/round and group.
 * <p/>
 * You can access tournament and rounds directly via the manager but groups are available only through
 * the {@code TournamentSearchManager} because there are a lot of groups and games in one tournament.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentManager extends SearchManager<TournamentEntity, TournamentEntityCtx<TournamentEntity>, SearchFilter> {
	void addTournamentStateListener(TournamentStateListener l);

	void removeTournamentStateListener(TournamentStateListener l);

	void addTournamentProgressListener(TournamentProgressListener l);

	void removeTournamentProgressListener(TournamentProgressListener l);

	void addTournamentSubscriptionListener(TournamentSubscriptionListener l);

	void removeTournamentSubscriptionListener(TournamentSubscriptionListener l);


	/**
	 * Returns next scheduled tournament or {@code null} if there is no scheduled tournaments.
	 *
	 * @return the next scheduled tournament or {@code null} if there is no scheduled tournaments.
	 */
	TournamentAnnouncement getTournamentAnnouncement();

	/**
	 * Subscribes specified player to the tournament.
	 * <p/>
	 * If player is not subscribed nothing will happen.
	 *
	 * @param tournament the number of a tournament. The number is used for user actions synchronization with
	 *                   current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
	 * @param player     the player who would like take part in the tournament.
	 * @param language   the language of the tournament.
	 * @param section    the section to be subscribed to
	 * @return original request or {@code null} if player wasn't subscribed.
	 * @throws WrongTournamentException if specified announcement doesn't equals to specified.
	 */
	TournamentSubscription subscribe(int tournament, Player player, Language language, TournamentSection section) throws WrongTournamentException, WrongSectionException;

	/**
	 * Unsubscribes specified player from the tournament.
	 * <p/>
	 * If player is not subscribed nothing will happen.
	 *
	 * @param tournament the number of a tournament. The number is used for user actions synchronization with
	 *                   current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
	 * @param player     the player who would like take part in the tournament.
	 * @param language   the language of the tournament.
	 * @return original request or {@code null} if player wasn't subscribed.
	 * @throws WrongTournamentException if specified announcement doesn't equals to specified.
	 */
	TournamentSubscription unsubscribe(int tournament, Player player, Language language) throws WrongTournamentException;

	/**
	 * Returns current request for the player.
	 *
	 * @param tournament the number of a tournament. The number is used for user actions synchronization with
	 *                   current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
	 * @param player     the player who's request should be returned.
	 * @param language   the language of the tournament.
	 * @return the player's request or {@code null} if plyaer is not subscribed to the tournament.
	 * @throws WrongTournamentException if specified announcement doesn't equals to specified.
	 */
	TournamentSubscription getSubscription(int tournament, Player player, Language language) throws WrongTournamentException;


	/**
	 * Returns tournament with specified number or {@code null} if there is no tournament.
	 *
	 * @param number tournament number
	 * @return the tournament with specified number or {@code null} if there is no tournament.
	 */
	Tournament getTournament(int number);

	/**
	 * Returns tournament round by specified id or {@code null} if there is no one.
	 *
	 * @param tournament the tournament id
	 * @param language   the tournament language
	 * @param section    the tournament section
	 * @param round      the tournament round  id
	 * @return the tournament round by specified id or {@code null} if there is no one.
	 */
	TournamentRound getTournamentRound(int tournament, Language language, TournamentSection section, int round);

	/**
	 * Returns tournament group by specified group id or {@code null} if there is no group.
	 *
	 * @param tournament the tournament id
	 * @param language   the tournament language
	 * @param section    the tournament section
	 * @param round      the tournament round id
	 * @param group      the group id.
	 * @return the tournament group by specified group id or {@code null} if there is no group.
	 */
	TournamentGroup getTournamentGroup(int tournament, Language language, TournamentSection section, int round, int group);
}
