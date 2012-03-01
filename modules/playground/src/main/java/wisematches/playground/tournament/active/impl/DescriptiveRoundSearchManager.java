package wisematches.playground.tournament.active.impl;

import wisematches.playground.search.descriptive.AbstractDescriptiveSearchManager;
import wisematches.playground.search.SearchCriteria;
import wisematches.playground.tournament.TournamentRound;
import wisematches.playground.tournament.TournamentSectionId;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DescriptiveRoundSearchManager extends AbstractDescriptiveSearchManager<TournamentRound, TournamentSectionId> {
	public DescriptiveRoundSearchManager() {
		super(TournamentRound.class);
	}

	@Override
	protected String getEntitiesList(TournamentSectionId context, SearchCriteria[] criteria) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getWhereCriterias(TournamentSectionId context, SearchCriteria[] criteria) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getGroupCriterias(TournamentSectionId context, SearchCriteria[] criteria) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
