package wisematches.playground.tourney;

import wisematches.playground.search.descriptive.SearchableBean;

import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;

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
	 * Returns current entity state. The state is based on started and finished dates.
	 *
	 * @return the current entity state.
	 */
	State getState();

	/**
	 * Returns date when the entity has been started.
	 *
	 * @return the date when the entity has been started.
	 */
	Date getStartedDate();

	/**
	 * Returns date when the entity has been finished.
	 *
	 * @return the date when the entity has been finished.
	 */
	Date getFinishedDate();

	/**
	 * Indicates current state of the entity.
	 */
	public enum State {
		/**
		 * Indicates that entity was scheduled or not ready yet.
		 */
		SCHEDULED,
		/**
		 * Indicates that entity is active at this moment.
		 */
		ACTIVE,
		/**
		 * Indicates that entity was finished already.
		 */
		FINISHED;

		public static State getState(Date started, Date finished) {
			if (finished != null) {
				return State.FINISHED;
			}
			if (started != null) {
				return State.ACTIVE;
			}
			return State.SCHEDULED;
		}
	}

	/**
	 * The entity id
	 *
	 * @param <T> the entity type that is represented by this id
	 * @param <I> the entity id type.
	 */
	public abstract class Id<T extends TourneyEntity<T, I, ?>, I extends TourneyEntity.Id<T, I>> implements Serializable {
		protected Id() {
		}

		@Override
		public abstract int hashCode();

		@Override
		public abstract boolean equals(Object obj);

		@Override
		public abstract String toString();
	}

	/**
	 * The entity search context.
	 *
	 * @param <T> the entity type that is represented by this entity
	 * @param <C> the entity context type.
	 */
	public abstract class Context<T extends TourneyEntity<T, ?, C>, C extends TourneyEntity.Context<T, C>> implements Serializable {
		private final EnumSet<State> states;

		protected Context() {
			this(null);
		}

		protected Context(EnumSet<State> states) {
			this.states = states;
		}

		public EnumSet<State> getStates() {
			return states;
		}
	}
}
