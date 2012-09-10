package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;

import java.util.Date;

/**
 * {@code TournamentDivision} is main block of tournament. Each tournament division has
 * language and section.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyDivision extends RegularTourneyElement<RegularTourneyDivision, RegularTourneyDivision.Id, RegularTourneyDivision.Context> {
	/**
	 * Returns division language
	 *
	 * @return the division language.
	 */
	Language getLanguage();

	/**
	 * Returns division section.
	 *
	 * @return the division section.
	 */
	RegularTourneySection getSection();

	Date getStartedDate();

	Date getFinishedDate();


	/**
	 * Returns active round for this division starting with one.
	 *
	 * @return the active round or {@code zero} if division is finished.
	 */
	int getActiveRound();


	public final class Id implements TourneyEntity.Id<RegularTourneyDivision, Id> {
	}

	public final class Context implements TourneyEntity.Context<RegularTourneyDivision, Context> {
		private final int tournament;
		private final Language language;
		private final RegularTourneySection section;

		public Context(int tournament, Language language, RegularTourneySection section) {
			this.tournament = tournament;
			this.language = language;
			this.section = section;
		}

		public int getTournament() {
			return tournament;
		}

		public Language getLanguage() {
			return language;
		}

		public RegularTourneySection getSection() {
			return section;
		}
	}
}