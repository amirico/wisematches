package wisematches.playground.tournament.active.impl;

import wisematches.playground.search.descriptive.AbstractDescriptiveSearchManager;
import wisematches.playground.search.SearchCriteria;
import wisematches.playground.tournament.TournamentId;
import wisematches.playground.tournament.TournamentSection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class DescriptiveSectionSearchManager extends AbstractDescriptiveSearchManager<TournamentSection, TournamentId> {
	protected DescriptiveSectionSearchManager() {
		super(TournamentSection.class);
	}

	@Override
	protected String getEntitiesList(TournamentId context, SearchCriteria[] criteria) {
		return null;
	}

	@Override
	protected String getWhereCriterias(TournamentId context, SearchCriteria[] criteria) {
		return null;
	}

	@Override
	protected String getGroupCriterias(TournamentId context, SearchCriteria[] criteria) {
		return null;
	}
}
