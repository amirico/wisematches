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
