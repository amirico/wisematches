package wisematches.playground.tourney;

import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;

import java.util.List;

/**
 * {@code TourneyEntityManager} is base interface for tourneys and allows access to tourney entities, like
 * rounds, groups and sections.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyEntityManager<E extends TourneyEntity>
        extends SearchManager<E, TourneyEntity.Context<? extends E, ?>, SearchFilter> {

    <T extends E, K extends TourneyEntity.Id<? extends T, ?>> T getTourneyEntity(K id);

    <T extends E, C extends TourneyEntity.Context<? extends T, ?>> List<T> searchTourneyEntities(Personality person, C context, SearchFilter filter, Orders orders, Range range);
}
