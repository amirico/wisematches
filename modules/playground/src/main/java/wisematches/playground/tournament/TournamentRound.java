package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.util.Date;

/**
 * A tournament round belong to a tournament and
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class TournamentRound extends TournamentEntity {
	protected TournamentRound() {
	}

	/**
	 * Returns number of the round.
	 *
	 * @return the number of the round.
	 */
	public abstract int getRound();

	/**
	 * Returns tournament that contains this round.
	 *
	 * @return the tournament that contains this round.
	 */
	public abstract int getTournament();

	/**
	 * Returns language for the round.
	 *
	 * @return the language for the round.
	 */
	public abstract Language getLanguage();

	/**
	 * Returns section of this round.
	 *
	 * @return the section of the round.
	 */
	public abstract TournamentSection getSection();


	/**
	 * Returns date when round was started.
	 *
	 * @return the date when round was started.
	 */
	public abstract Date getStartedDate();

	/**
	 * Returns date when round was finished.
	 *
	 * @return the date when round was finished or {@code null} if round is in progress.
	 */
	public abstract Date getFinishedDate();


	/**
	 * Returns number of total games in the round.
	 *
	 * @return the number of total games in the round.
	 */
	public abstract int getTotalGamesCount();

	/**
	 * Returns number of finished games.
	 *
	 * @return the number of finished games.
	 */
	public abstract int getFinishedGamesCount();


	public static Id createId(int tournament, Language language, TournamentSection section, int round) {
		return new Id(tournament, language, section, round);
	}

	public static Context createContext(int tournament, Language language, TournamentSection section) {
		return new Context(tournament, language, section);
	}


	public static class Id implements TournamentEntityId<TournamentRound> {
		private final int tournament;
		private final Language language;
		private final TournamentSection section;
		private final int round;

		public Id(int tournament, Language language, TournamentSection section, int round) {
			this.tournament = tournament;
			this.language = language;
			this.section = section;
			this.round = round;
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

		public int getRound() {
			return round;
		}
	}

	public static class Context implements TournamentEntityContext<TournamentRound> {
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
