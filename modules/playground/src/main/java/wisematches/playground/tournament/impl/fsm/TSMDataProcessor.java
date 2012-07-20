package wisematches.playground.tournament.impl.fsm;

import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentGroup;
import wisematches.playground.tournament.TournamentRound;
import wisematches.playground.tournament.TournamentSubscription;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TSMDataProcessor {
	TournamentGroup searchGroup(long boardId);

	TournamentGroup startGame(TournamentGroup.Id group, long[] games);

	TournamentGroup finishGame(TournamentGroup.Id group, long game, short[] points);


//	<T extends TournamentEntity> T startEntity(TournamentEntity.Id<T> entity, Object parameters);
//
//	<T extends TournamentEntity> T finishEntity(TournamentEntity.Id<T> entity, Object parameters);

	/**
	 * Returns collection of all tournaments which should be started right now.
	 *
	 * @return the collection of all tournaments which should be started right now.
	 */
	Collection<Tournament> getReadyTournaments();

	/**
	 * Returns collection of all groups which were finished but weren't processed yet.
	 *
	 * @return the collection of all groups which were finished but weren't processed yet.
	 */
	Collection<TournamentGroup> getReadyGroups();

	/**
	 * Returns collection of rounds which should be started.
	 *
	 * @return the collection of tournament rounds.
	 */
	Collection<TournamentRound> getReadyRounds();

	/**
	 * Returns list of unprocessed players for specified tournament and round.
	 * <p/>
	 * The method returns list of players which should be included in next round but
	 * wasn't processed and games were not created yet. If round is zero - it's first
	 * round and subscribed players should be returned.
	 *
	 * @param tournament the tournament id.
	 * @param round      the round id starting with zero
	 * @return the collection of all unprocessed players who should take part in next round.
	 */
	Collection<TournamentSubscription> getUnprocessedPlayers(int tournament, int round);

}
