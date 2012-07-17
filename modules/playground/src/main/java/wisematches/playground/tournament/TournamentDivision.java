package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.util.Date;

/**
 * {@code TournamentDivision} is main block of tournament. Each tournament division has
 * language and section.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class TournamentDivision extends TournamentEntity {
	protected TournamentDivision() {
	}

	/**
	 * Returns tournament round.
	 *
	 * @return the tournament round.
	 */
	public abstract int getTournament();

	/**
	 * Returns division language
	 *
	 * @return the division language.
	 */
	public abstract Language getLanguage();

	/**
	 * Returns division section.
	 *
	 * @return the division section.
	 */
	public abstract TournamentSection getSection();


	/**
	 * Returns active round for this division starting with one.
	 *
	 * @return the active round or {@code zero} if division is finished.
	 */
	public abstract int getActiveRound();


	/**
	 * Returns date when the division was started.
	 *
	 * @return date when the division was started.
	 */
	public abstract Date getStartedDate();


	/**
	 * Returns data when the division was finished or {@code null} if the division is active.
	 *
	 * @return the date when the division was finished or {@code null} if the division is active.
	 */
	public abstract Date getFinishedDate();


	public static Id createId(final int tournament, final Language language, final TournamentSection section) {
		return new Id(tournament, language, section);
	}

	public static Context createContext(int tournament) {
		return new Context(tournament, null, null);
	}

	public static Context createContext(int tournament, Language language) {
		return new Context(tournament, language, null);
	}

	public static Context createContext(int tournament, TournamentSection section) {
		return new Context(tournament, null, section);
	}


	public static class Id implements TournamentEntityId<TournamentDivision> {
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

	public static class Context implements TournamentEntityContext<TournamentDivision> {
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