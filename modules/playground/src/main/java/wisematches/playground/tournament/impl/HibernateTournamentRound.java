package wisematches.playground.tournament.impl;

import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentRound;
import wisematches.playground.tournament.TournamentSection;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
/*
@Entity
@Table(name = "tournament_round")
*/
public class HibernateTournamentRound implements TournamentRound {
	@Override
	public int getRound() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getTournament() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Language getLanguage() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public TournamentSection getSectionType() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Date getStartDate() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Date getFinishDate() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getTotalGames() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getFinishedGames() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public boolean isFinished() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}
