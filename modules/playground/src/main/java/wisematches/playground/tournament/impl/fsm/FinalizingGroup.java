package wisematches.playground.tournament.impl.fsm;

import wisematches.playground.tournament.TournamentGroupCtx;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FinalizingGroup implements TSMActivityContext {
	private final TournamentGroupCtx groupCtx;

	public FinalizingGroup(TournamentGroupCtx groupCtx) {
		this.groupCtx = groupCtx;
	}

	public TournamentGroupCtx getGroupCtx() {
		return groupCtx;
	}
}
