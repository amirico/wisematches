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
public class HibernateTournamentRound extends TournamentRound {
	public HibernateTournamentRound() {
	}

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
	public int getTotalGamesCount() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public int getFinishedGamesCount() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
}
