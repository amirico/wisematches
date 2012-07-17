package wisematches.playground.tournament.impl.fsm;

import wisematches.playground.tournament.TournamentGroup;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TSMFinalizingGroup implements TSMActivityContext {
	private final TournamentGroup.Context groupCtx;

	public TSMFinalizingGroup(TournamentGroup.Context groupCtx) {
		this.groupCtx = groupCtx;
	}

	public TournamentGroup.Context getGroupCtx() {
		return groupCtx;
	}
}
