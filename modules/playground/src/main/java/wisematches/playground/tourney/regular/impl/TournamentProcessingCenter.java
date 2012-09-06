package wisematches.playground.tourney.regular.impl;

import wisematches.playground.timer.BreakingDayListener;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentProcessingCenter implements BreakingDayListener {
	public TournamentProcessingCenter() {
	}

	@Override
	public void breakingDayTime(Date date) {
/*
		TourneyManager m = new TourneyManager() {
			@Override
			public <T extends TourneyEntity<T, I, ?>, I extends TourneyEntity.Id<T, I>> T getTournamentEntity(I id) {
				throw new UnsupportedOperationException("TODO: Not implemented");
			}
		};

		final RegularTourney tournamentEntity = m.getTournamentEntity(new RegularTourney.Id(12L));
		final VoluntaryTourney.Id parentId1 = tournamentEntity.getParentId();

		throw new UnsupportedOperationException("TODO: Not implemented");
*/
	}
}
