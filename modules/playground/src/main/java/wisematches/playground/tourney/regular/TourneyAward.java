package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyPlace;

import java.util.Date;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public interface TourneyAward {
	int getTourney();

	Date getAwardedDate();

	Language getLanguage();

	TourneyPlace getMedal();

	TourneySection getSection();

	final static class Context {
		private final EnumSet<TourneyPlace> medals;

		public Context() {
			this(null);
		}

		public Context(EnumSet<TourneyPlace> medals) {
			this.medals = medals;
		}

		public EnumSet<TourneyPlace> getMedals() {
			return medals;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Context");
			sb.append("{medals=").append(medals);
			sb.append('}');
			return sb.toString();
		}
	}
}
