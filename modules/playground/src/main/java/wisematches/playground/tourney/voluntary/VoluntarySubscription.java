package wisematches.playground.tourney.voluntary;

import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneySubscription;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface VoluntarySubscription extends TourneySubscription<VoluntarySubscription, VoluntarySubscription.Id, VoluntarySubscription.Context> {
	public static class Id implements TourneyEntity.Id<VoluntarySubscription, Id> {
	}

	public static class Context implements TourneyEntity.Context<VoluntarySubscription, Context> {
	}
}
