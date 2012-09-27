package wisematches.playground.tourney.regular;

import wisematches.playground.tourney.TourneyEntity;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourney extends RegularTourneyEntity<RegularTourney, RegularTourney.Id, RegularTourney.Context> {
	/**
	 * Returns tourney number
	 *
	 * @return the tourney number.
	 */
	int getNumber();

	/**
	 * Returns date when the tournament should be started/was started
	 *
	 * @return date when the tournament should be started/was started
	 */
	Date getScheduledDate();

	public final class Id implements TourneyEntity.Id<RegularTourney, Id> {
		private final int number;

		public Id(int number) {
			this.number = number;
		}

		public int getNumber() {
			return number;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Id id = (Id) o;
			return number == id.number;
		}

		@Override
		public int hashCode() {
			return number;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Id");
			sb.append("{number=").append(number);
			sb.append('}');
			return sb.toString();
		}
	}

	public final class Context implements TourneyEntity.Context<RegularTourney, Context> {
		public Context() {
		}
	}
}
