package wisematches.playground.tourney.voluntary;

import wisematches.playground.tourney.TourneyElement;
import wisematches.playground.tourney.TourneyEntity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface VoluntaryTourneyElement<
		T extends VoluntaryTourneyElement<T, I, C>,
		I extends TourneyEntity.Id<T, I>,
		C extends TourneyEntity.Context<T, C>> extends VoluntaryTourneyEntity<T, I, C>, TourneyElement<T, I, C> {
}
