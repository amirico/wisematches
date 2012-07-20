package wisematches.playground.tournament.impl;

import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentDivision;
import wisematches.playground.tournament.TournamentSection;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentDivision implements TournamentDivision {
	public HibernateTournamentDivision() {
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
	public TournamentSection getSection() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Date getStartedDate() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public Date getFinishedDate() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getActiveRound() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}
