package wisematches.playground.tournament;

import wisematches.playground.search.descriptive.SearchableBean;

import java.io.Serializable;

/**
 * {@code TournamentEntity} is base marker interface for any tournament entity, like division, round or group.
 * <p/>
 * Each entity has special {@link TournamentEntity.Id} which identifies entity and {@link TournamentEntity.Context} context
 * that allows selec list of entities by some criterias.
 *
 * @param <T> the entity type that is represented by this entity
 * @param <I> the entity id type
 * @param <C> the entity context type.
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SearchableBean
public interface TournamentEntity<T extends TournamentEntity<T, I, C>, I extends TournamentEntity.Id<T, I, C>, C extends TournamentEntity.Context<T, I, C>> extends Serializable {
	/**
	 * Returns tournament id.
	 *
	 * @return the tournament id.
	 */
	int getTournament();

	/**
	 * The entity id interface.
	 *
	 * @param <T> the entity type that is represented by this entity
	 * @param <I> the entity id type
	 * @param <C> the entity context type.
	 */
	public interface Id<T extends TournamentEntity<T, I, C>, I extends TournamentEntity.Id<T, I, C>, C extends TournamentEntity.Context<T, I, C>> extends Serializable {
		/**
		 * Returns tournament id.
		 *
		 * @return the tournament id.
		 */
		int getTournament();
	}

	/**
	 * The entity search context.
	 *
	 * @param <T> the entity type that is represented by this entity
	 * @param <I> the entity id type
	 * @param <C> the entity context type.
	 */
	public interface Context<T extends TournamentEntity<T, I, C>, I extends TournamentEntity.Id<T, I, C>, C extends TournamentEntity.Context<T, I, C>> extends Serializable {
	}
}
