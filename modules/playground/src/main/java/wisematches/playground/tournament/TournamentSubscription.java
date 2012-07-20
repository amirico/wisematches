package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * The tournament request is player's application for upcoming tournament. It contains
 * player, section and language.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentSubscription extends TournamentEntity<TournamentSubscription, TournamentSubscription.Id, TournamentSubscription.Context> {
	/**
	 * Returns number of a tournament.
	 *
	 * @return the number of a tournament.
	 */
	int getTournament();

	/**
	 * Returns player id who sent the request
	 *
	 * @return the player id who sent the request
	 */
	long getPlayer();

	/**
	 * Returns tournament's language.
	 *
	 * @return the tournament's language.
	 */
	Language getLanguage();

	/**
	 * Returns tournament's section.
	 *
	 * @return the tournament's section.
	 */
	TournamentSection getSection();


	public final class Id implements TournamentEntity.Id<TournamentSubscription, Id, Context> {
		private final int tournament;
		private final long player;
		private final Language language;

		public Id(int tournament, long player, Language language) {
			this.tournament = tournament;
			this.player = player;
			this.language = language;
		}

		public int getTournament() {
			return tournament;
		}

		public long getPlayer() {
			return player;
		}

		public Language getLanguage() {
			return language;
		}
	}


	public final class Context implements TournamentEntity.Context<TournamentSubscription, Id, Context> {
		private final int tournament;
		private final long player;

		public Context(int tournament, long player) {
			this.tournament = tournament;
			this.player = player;
		}

		public int getTournament() {
			return tournament;
		}

		public long getPlayer() {
			return player;
		}
	}
}
