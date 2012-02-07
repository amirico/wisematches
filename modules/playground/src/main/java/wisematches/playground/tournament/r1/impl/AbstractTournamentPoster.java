package wisematches.playground.tournament.r1.impl;

import wisematches.playground.tournament.r1.TournamentPoster;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractTournamentPoster implements TournamentPoster {
	protected AbstractTournamentPoster() {
	}

	protected abstract void setStarted(boolean started);

	protected abstract void setProcessed(boolean processed);
}
