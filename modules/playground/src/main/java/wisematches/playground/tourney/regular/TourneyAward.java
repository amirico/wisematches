package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyMedal;

import java.util.Date;
import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyAward {
	int getTourney();

	Date getAwardedDate();

	Language getLanguage();

	TourneyMedal getMedal();

	TourneySection getSection();

	final static class Context {
		private final EnumSet<TourneyMedal> medals;

		public Context() {
			this(null);
		}

		public Context(EnumSet<TourneyMedal> medals) {
			this.medals = medals;
		}

		public EnumSet<TourneyMedal> getMedals() {
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
