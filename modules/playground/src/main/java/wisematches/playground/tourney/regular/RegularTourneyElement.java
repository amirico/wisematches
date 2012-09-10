package wisematches.playground.tourney.regular;

import wisematches.playground.tourney.TourneyElement;
import wisematches.playground.tourney.TourneyEntity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyElement<
		T extends RegularTourneyElement<T, I, C>,
		I extends TourneyEntity.Id<T, I>,
		C extends TourneyEntity.Context<T, C>> extends TourneyElement<T, I, C>, RegularTourneyEntity<T, I, C> {
}
