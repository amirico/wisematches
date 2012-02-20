package wisematches.playground.tournament.active.impl;

import wisematches.playground.search.AbstractHibernateSearchManager;
import wisematches.playground.tournament.TournamentGroup;
import wisematches.playground.tournament.TournamentRoundId;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class HibernateGroupSearchManager extends AbstractHibernateSearchManager<TournamentGroup, TournamentRoundId> {
	protected HibernateGroupSearchManager() {
		super(TournamentGroup.class);
	}

	@Override
	protected String getTablesList(TournamentRoundId context) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getWhereCriterias(TournamentRoundId context) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getGroupCriterias(TournamentRoundId context) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
