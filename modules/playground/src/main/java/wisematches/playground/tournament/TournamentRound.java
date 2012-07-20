package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.util.Date;

/**
 * A tournament round belong to a tournament and
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentRound extends TournamentEntity<TournamentRound, TournamentRound.Id, TournamentRound.Context> {
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
	TournamentSection getSection();

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


	public static class Id implements TournamentEntity.Id<TournamentRound, Id, Context> {
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

	public static class Context implements TournamentEntity.Context<TournamentRound, Id, Context> {
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
