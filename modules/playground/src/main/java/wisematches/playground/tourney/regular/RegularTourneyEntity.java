package wisematches.playground.tourney.regular;

import wisematches.playground.tourney.TourneyEntity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyEntity<
		T extends RegularTourneyEntity<T, I, C>,
		I extends TourneyEntity.Id<T, I>,
		C extends TourneyEntity.Context<T, C>> extends TourneyEntity<T, I, C> {

	int getTournament();
}
