package wisematches.playground.tourney.regular.impl;

import wisematches.playground.tourney.TourneyEntity;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentEntityDao {
	/**
	 * Marks specified tournament entity as processed.
	 *
	 * @param entity the tournament entity that should be marked as processed.
	 */
	void markTournamentEntityProcessed(TourneyEntity entity);

	/**
	 * Returns collection of all unprocessed tournament entities for specified type
	 *
	 * @param type tournament entity's type
	 * @param <T>  tournament entity's type
	 * @return collection of all unprocessed tournament entities for specified type.
	 */
	<T extends TourneyEntity> Collection<T> getUnprocessedTournamentEntities(Class<T> type);
}
