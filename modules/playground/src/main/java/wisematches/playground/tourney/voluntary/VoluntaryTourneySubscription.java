package wisematches.playground.tourney.voluntary;

import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneySubscription;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface VoluntaryTourneySubscription extends
		TourneyEntity<VoluntaryTourneySubscription, VoluntaryTourneySubscription.Id, VoluntaryTourneySubscription.Context>,
		TourneySubscription<VoluntaryTourneySubscription, VoluntaryTourneySubscription.Id, VoluntaryTourneySubscription.Context> {
	public static class Id implements TourneyEntity.Id<VoluntaryTourneySubscription, Id> {
	}

	public static class Context implements TourneyEntity.Context<VoluntaryTourneySubscription, Context> {
	}
}
