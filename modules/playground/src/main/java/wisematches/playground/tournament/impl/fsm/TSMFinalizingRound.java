package wisematches.playground.tournament.impl.fsm;

import wisematches.playground.tournament.TournamentGroup;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TSMFinalizingRound implements TSMActivityContext {
	private final TournamentGroup.Context roundContext;

	public TSMFinalizingRound(TournamentGroup.Context roundContext) {
		this.roundContext = roundContext;
	}

	public TournamentGroup.Context getRoundContext() {
		return roundContext;
	}
}
