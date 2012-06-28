package wisematches.playground.tournament.impl.fsm;

import wisematches.personality.Personality;
import wisematches.playground.tournament.TournamentGroupCtx;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class InitializingGroup implements TSMActivityContext {
	private final Personality[] players;
	private final TournamentGroupCtx groupContext;

	private static final long serialVersionUID = -6365203646834709656L;

	public InitializingGroup(TournamentGroupCtx groupContext, Personality[] players) {
		this.groupContext = groupContext;
		this.players = players;
	}

	public Personality[] getPlayers() {
		return players;
	}

	public TournamentGroupCtx getGroupContext() {
		return groupContext;
	}
}
