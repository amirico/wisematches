package wisematches.playground.tournament.impl.fsm;

import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentGroup;
import wisematches.playground.tournament.TournamentRound;
import wisematches.playground.tournament.TournamentSection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TSMEventProcessor {
	void fireTournamentStarted(Tournament tournament);

	void fireTournamentFinished(Tournament tournament);


	void fireGroupStarted(TournamentGroup group);

	void fireGroupFinished(TournamentGroup group);


	void fireRoundStarted(TournamentRound round);

	void fireRoundFinished(TournamentRound round);


	void fireSectionStarted(Tournament tournament, TournamentSection section);

	void fireSectionFinished(Tournament tournament, TournamentSection section);
}
