package wisematches.playground.tournament.impl.fsm;

import wisematches.personality.Personality;
import wisematches.playground.tournament.TournamentGroup;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TSMInitializingGroup implements TSMActivityContext {
	private final Personality[] players;
	private final TournamentGroup.Context groupContext;

	private static final long serialVersionUID = -6365203646834709656L;

	public TSMInitializingGroup(TournamentGroup.Context groupContext, Personality[] players) {
		this.groupContext = groupContext;
		this.players = players;
	}

	public Personality[] getPlayers() {
		return players;
	}

	public TournamentGroup.Context getGroupContext() {
		return groupContext;
	}
}
