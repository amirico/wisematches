package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;

import java.util.Date;

/**
 * A tournament round belong to a tournament and
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyRound extends RegularTourneyElement<RegularTourneyRound, RegularTourneyRound.Id, RegularTourneyRound.Context> {
	int getTournament();

	/**
	 * Returns language for the round.
	 *
	 * @return the language for the round.
	 */
	Language getLanguage();

	/**
	 * Returns section of this round.
	 *
	 * @return the section of the round.
	 */
	RegularTourneySection getSection();

	/**
	 * Returns number of the round.
	 *
	 * @return the number of the round.
	 */
	int getRound();


	Date getStartedDate();

	Date getFinishedDate();


	/**
	 * Returns number of total games in the round.
	 *
	 * @return the number of total games in the round.
	 */
	int getTotalGamesCount();

	/**
	 * Returns number of finished games.
	 *
	 * @return the number of finished games.
	 */
	int getFinishedGamesCount();


	public final class Id implements TourneyEntity.Id<RegularTourneyRound, Id> {

	}

	public final class Context implements TourneyEntity.Context<RegularTourneyRound, Context> {
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
