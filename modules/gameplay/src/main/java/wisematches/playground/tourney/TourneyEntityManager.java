package wisematches.playground.tourney;

import wisematches.core.Personality;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.core.search.SearchManager;

import java.util.List;

/**
 * {@code TourneyEntityManager} is base interface for tourneys and allows access to tourney entities, like
 * rounds, groups and sections.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyEntityManager<E extends TourneyEntity>
		extends SearchManager<E, TourneyEntity.Context<? extends E, ?>> {
	<T extends E, K extends TourneyEntity.Id<? extends T, ?>> T getTourneyEntity(K id);

	<T extends E, C extends TourneyEntity.Context<? extends T, ?>> List<T> searchTourneyEntities(Personality person, C context, Orders orders, Range range);
}
