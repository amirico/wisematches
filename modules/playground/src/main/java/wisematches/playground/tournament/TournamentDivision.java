package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.util.Date;

/**
 * {@code TournamentDivision} is main block of tournament. Each tournament division has
 * language and section.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentDivision extends TournamentEntity<TournamentDivision, TournamentDivision.Id, TournamentDivision.Context> {
	int getTournament();

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
	TournamentSection getSection();

	Date getStartedDate();

	Date getFinishedDate();


	/**
	 * Returns active round for this division starting with one.
	 *
	 * @return the active round or {@code zero} if division is finished.
	 */
	int getActiveRound();


	public final class Id implements TournamentEntity.Id<TournamentDivision, Id, Context> {
		private final int tournament;
		private final Language language;
		private final TournamentSection section;

		public Id(int tournament, Language language, TournamentSection section) {
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

		public TournamentSection getSection() {
			return section;
		}
	}

	public final class Context implements TournamentEntity.Context<TournamentDivision, Id, Context> {
		private final int tournament;
		private final Language language;
		private final TournamentSection section;

		public Context(int tournament, Language language, TournamentSection section) {
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

		public TournamentSection getSection() {
			return section;
		}
	}
}