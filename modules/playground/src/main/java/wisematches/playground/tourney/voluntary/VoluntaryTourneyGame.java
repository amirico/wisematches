package wisematches.playground.tourney.voluntary;

import wisematches.playground.tourney.TourneyEntity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface VoluntaryTourneyGame extends VoluntaryTourneyElement<VoluntaryTourneyGame, VoluntaryTourneyGame.Id, VoluntaryTourneyGame.Context> {
	public final static class Id implements TourneyEntity.Id<VoluntaryTourneyGame, Id> {
	}

	public final static class Context implements TourneyEntity.Context<VoluntaryTourneyGame, Context> {
	}
}
