package wisematches.playground.tournament;

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
public interface TournamentManager extends SearchManager<TournamentEntity<? extends TournamentEntityId>, TournamentEntityId, SearchFilter> {
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
}
