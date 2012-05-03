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
public interface TournamentManager<E extends TournamentEntity<C>, C extends TournamentEntityId> extends SearchManager<E, C> {
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
}
