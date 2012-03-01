package wisematches.playground.tournament.active.impl;

import wisematches.playground.search.descriptive.AbstractDescriptiveSearchManager;
import wisematches.playground.search.SearchCriteria;
import wisematches.playground.tournament.TournamentGroup;
import wisematches.playground.tournament.TournamentRoundId;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class DescriptiveGroupSearchManager extends AbstractDescriptiveSearchManager<TournamentGroup, TournamentRoundId> {
	protected DescriptiveGroupSearchManager() {
		super(TournamentGroup.class);
	}

	@Override
	protected String getEntitiesList(TournamentRoundId context, SearchCriteria[] criteria) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getWhereCriterias(TournamentRoundId context, SearchCriteria[] criteria) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getGroupCriterias(TournamentRoundId context, SearchCriteria[] criteria) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
