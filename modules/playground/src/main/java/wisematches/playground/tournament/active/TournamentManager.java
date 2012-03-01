package wisematches.playground.tournament.active;

import wisematches.personality.player.Player;
import wisematches.playground.search.SearchManager;
import wisematches.playground.tournament.*;

import java.util.Collection;

/**
 * {@code TournamentManager} provides access to active tournaments/round and group.
 * <p/>
 * You can access tournament and rounds directly via the manager but groups are available only through
 * the {@code TournamentSearchManager} because there are a lot of groups and games in one tournament.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentManager {
	void addTournamentListener(TournamentListener l);

	void removeTournamentListener(TournamentListener l);

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
	 * @param roundId the tournament round id
	 * @return the tournament round by specified id or {@code null} if there is no one.
	 */
	TournamentRound getTournamentRound(TournamentRoundId roundId);

	/**
	 * Returns tournament group by specified group id or {@code null} if there is no group.
	 *
	 * @param groupId the group id.
	 * @return the tournament group by specified group id or {@code null} if there is no group.
	 */
	TournamentGroup getTournamentGroup(TournamentGroupId groupId);


	/**
	 * Returns collection of all active tournaments. If there is no active tournaments empty collection
	 * will be returned.
	 *
	 * @return collection of all active tournaments or empty collection.
	 */
	Collection<Tournament> getActiveTournaments();

	/**
	 * Returns collection of all active tournaments for specified player.
	 *
	 * @param player the player who tournaments should be returned.
	 * @return collection of player's tournaments or empty collection.
	 */
	Collection<Tournament> getPlayerTournaments(Player player);

	/**
	 * Returns collection of all rounds for specified tournament, language and section.
	 *
	 * @param sectionId returns list of all rounds for specified section.
	 * @return collection of all round in the tournament for specified language and section. If there are no rounds
	 *         empty collection will be returned. If there is no tournament {@code null} will be returned.
	 */
	Collection<TournamentRound> getTournamentRounds(TournamentSectionId sectionId);

	/**
	 * Returns tournament search manager that provides access to groups and games.
	 *
	 * @param type type of entity that should be searched.
	 * @return tournament search manager that provides access to groups and games.
	 */
	<E extends TournamentEntity<C>, C extends TournamentEntityId> SearchManager<E, C> getTournamentSearchManager(Class<E> type);
}
