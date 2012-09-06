package wisematches.playground.tourney.regular;

import wisematches.playground.tourney.TourneyEntity;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourney extends TourneyEntity<RegularTourney, RegularTourney.Id, RegularTourney.Context> {
	/**
	 * Returns number of this tournament.
	 *
	 * @return the number of this tournament.
	 */
	int getNumber();

	/**
	 * Returns scheduled date for this tournament.
	 *
	 * @return the scheduled date for this tournament.
	 */
	Date getScheduledDate();

	public final static class Id implements TourneyEntity.Id<RegularTourney, Id> {
	}

	/**
	 * Defines search context for any tournament entity.
	 */
	public final class Context implements TourneyEntity.Context<RegularTourney, Context> {
		private final State state;

		public Context(State state) {
			if (state == null) {
				throw new NullPointerException("Tournament state can't be null.");
			}
			this.state = state;
		}

		public State getState() {
			return state;
		}
	}
}
