package wisematches.playground.tourney.regular;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegistrationRecord {
	int getTourney();

	long getPlayer();

	long getRound();

	Language getLanguage();

	TourneySection getSection();

	final class Context {
		private final int round;
		private final int tourney;
		private final Language language;

		public Context(int round) {
			this(-1, null, round);
		}

		public Context(int tourney, int round) {
			this(tourney, null, round);
		}

		public Context(Language language, int round) {
			this(-1, language, round);
		}

		public Context(int tourney, Language language, int round) {
			this.round = round;
			this.tourney = tourney;
			this.language = language;
		}

		public int getRound() {
			return round;
		}

		public int getTourney() {
			return tourney;
		}

		public Language getLanguage() {
			return language;
		}
	}
}
