package wisematches.playground.tourney.voluntary;

import wisematches.playground.tourney.TourneyEntity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface VoluntaryTourneyEntity<
		T extends VoluntaryTourneyEntity<T, I, C>,
		I extends TourneyEntity.Id<T, I>,
		C extends TourneyEntity.Context<T, C>> extends TourneyEntity<T, I, C> {
}
