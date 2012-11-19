package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneySubscription extends RegularTourneyEntity<TourneySubscription, TourneySubscription.Id, TourneySubscription.Context> {
	int getTourney();

	long getPlayer();

	long getRound();

	Language getLanguage();

	TourneySection getSection();

	public final class Id extends TourneyEntity.Id<TourneySubscription, Id> {
		private final int tourney;
		private final long player;
		private final int round;

		public Id(int tourney, long player, int round) {
			this.round = round;
			this.player = player;
			this.tourney = tourney;
		}

		public int getTourney() {
			return tourney;
		}

		public long getPlayer() {
			return player;
		}

		public int getRound() {
			return round;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Id id = (Id) o;
			return player == id.player && round == id.round && tourney == id.tourney;
		}

		@Override
		public int hashCode() {
			int result = tourney;
			result = 31 * result + (int) (player ^ (player >>> 32));
			result = 31 * result + round;
			return result;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Id");
			sb.append("{tourney=").append(tourney);
			sb.append(", player=").append(player);
			sb.append(", round=").append(round);
			sb.append('}');
			return sb.toString();
		}
	}

	public final class Context extends TourneyEntity.Context<TourneySubscription, Context> {
		private int tourney;
		private Language language;

		public Context(int tourney) {
			this.tourney = tourney;
		}

		public Context(Language language) {
			this.language = language;
		}

		public Context(int tourney, Language language) {
			this.tourney = tourney;
			this.language = language;
		}

		public int getTourney() {
			return tourney;
		}

		public Language getLanguage() {
			return language;
		}
	}
}
