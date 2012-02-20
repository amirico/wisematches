package wisematches.playground.tournament.active.impl;

import wisematches.playground.search.AbstractHibernateSearchManager;
import wisematches.playground.tournament.TournamentId;
import wisematches.playground.tournament.TournamentSection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class HibernateSectionSearchManager extends AbstractHibernateSearchManager<TournamentSection, TournamentId> {
	protected HibernateSectionSearchManager() {
		super(TournamentSection.class);
	}

	@Override
	protected String getTablesList(TournamentId context) {
		return null;
	}

	@Override
	protected String getWhereCriterias(TournamentId context) {
		return null;
	}

	@Override
	protected String getGroupCriterias(TournamentId context) {
		return null;
	}
}
