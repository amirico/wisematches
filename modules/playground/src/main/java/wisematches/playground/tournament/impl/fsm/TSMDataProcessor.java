package wisematches.playground.tournament.impl.fsm;

import wisematches.personality.Personality;
import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentGroup;
import wisematches.playground.tournament.TournamentGroupCtx;
import wisematches.playground.tournament.TournamentSubscription;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TSMDataProcessor {
	Tournament startTournament(int number);

	Tournament finishTournament(int number);


	TournamentGroup getGameGroup(long boardId);

	TournamentGroup getGameGroup(TournamentGroupCtx group);

	TournamentGroup startGroup(TournamentGroupCtx group, Personality[] players);

	TournamentGroup startGroupGame(TournamentGroupCtx group, long[] games);

	TournamentGroup finishGroupGame(TournamentGroupCtx group, long game, short[] points);

	TournamentGroup finishGroup(TournamentGroupCtx group);


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

	Collection<TournamentSubscription> getUnprocessedPlayers(int tournament, int round);
}
