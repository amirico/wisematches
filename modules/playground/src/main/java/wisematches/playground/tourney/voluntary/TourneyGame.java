package wisematches.playground.tourney.voluntary;

import wisematches.playground.tourney.TourneyEntity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyGame extends VoluntaryTourneyEntity<TourneyGame, TourneyGame.Id, TourneyGame.Context> {
	public final static class Id implements TourneyEntity.Id<TourneyGame, Id> {
	}

	public final static class Context implements TourneyEntity.Context<TourneyGame, Context> {
	}
}
