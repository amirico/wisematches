package wisematches.playground.tourney;

import wisematches.playground.search.descriptive.SearchableBean;

import java.io.Serializable;
import java.util.Date;

/**
 * {@code TournamentEntity} is base marker interface for any tournament entity, like division, round or group.
 * <p/>
 * Each entity has special {@link TourneyEntity.Id} which identifies entity and {@link TourneyEntity.Context} context
 * that allows selec list of entities by some criterias.
 *
 * @param <T> the entity type that is represented by this entity
 * @param <C> the entity context type.
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SearchableBean
public interface TourneyEntity<T extends TourneyEntity<T, I, C>, I extends TourneyEntity.Id<T, I>, C extends TourneyEntity.Context<T, C>> extends Serializable {
	/**
	 * Returns unique id of the entity.
	 *
	 * @return the unique id of the entity.
	 */
	I getId();

	/**
	 * Returns started date for this tournament. Date can be null if the tournament is not started yet.
	 *
	 * @return started date for this tournament. Date can be null if the tournament is not started yet.
	 */
	Date getStartedDate();

	/**
	 * Returns date when the tournament was finished or null if it's not finished yet.
	 *
	 * @return the date when the tournament was finished or null if it's not finished yet.
	 */
	Date getFinishedDate();


	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	enum State {
		STARTED,

		FINISHED,

		SCHEDULED
	}

	/**
	 * The entity id
	 *
	 * @param <T> the entity type that is represented by this id
	 * @param <I> the entity id type.
	 */
	interface Id<T extends TourneyEntity<T, I, ?>, I extends TourneyEntity.Id<T, I>> extends Serializable {
	}

	/**
	 * The entity search context.
	 *
	 * @param <T> the entity type that is represented by this entity
	 * @param <C> the entity context type.
	 */
	interface Context<T extends TourneyEntity<T, ?, C>, C extends TourneyEntity.Context<T, C>> extends Serializable {
	}
}
