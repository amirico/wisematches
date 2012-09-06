package wisematches.playground.tourney.voluntary;

import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneyItem;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface VoluntaryTourneyGame extends TourneyItem<VoluntaryTourneyGame, VoluntaryTourneyGame.Id, VoluntaryTourneyGame.Context> {
	public final static class Id implements TourneyEntity.Id<VoluntaryTourneyGame, Id> {
	}

	public final static class Context implements TourneyEntity.Context<VoluntaryTourneyGame, Context> {
	}
}
