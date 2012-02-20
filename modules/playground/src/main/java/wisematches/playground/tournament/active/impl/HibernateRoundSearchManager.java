package wisematches.playground.tournament.active.impl;

import wisematches.playground.search.AbstractHibernateSearchManager;
import wisematches.playground.tournament.TournamentId;
import wisematches.playground.tournament.TournamentRound;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSectionId;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateRoundSearchManager extends AbstractHibernateSearchManager<TournamentRound, TournamentSectionId> {
	public HibernateRoundSearchManager() {
		super(TournamentRound.class);
	}

	@Override
	protected String getTablesList(TournamentSectionId context) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getWhereCriterias(TournamentSectionId context) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getGroupCriterias(TournamentSectionId context) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
