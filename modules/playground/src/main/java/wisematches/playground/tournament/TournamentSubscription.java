package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * The tournament request is player's application for upcoming tournament. It contains
 * player, section and language.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class TournamentSubscription extends TournamentEntity {
	protected TournamentSubscription() {
	}

	/**
	 * Returns player id who sent the request
	 *
	 * @return the player id who sent the request
	 */
	public abstract long getPlayer();

	/**
	 * Returns number of a tournament.
	 *
	 * @return the number of a tournament.
	 */
	public abstract int getTournament();

	/**
	 * Returns tournament's language.
	 *
	 * @return the tournament's language.
	 */
	public abstract Language getLanguage();

	/**
	 * Returns tournament's section.
	 *
	 * @return the tournament's section.
	 */
	public abstract TournamentSection getSection();


	public static Id createId(int tournament, long player, Language language) {
		return new Id(tournament, player, language);
	}

	public static Context createContext(int tournament, long player) {
		return new Context(tournament, player);
	}


	public static class Id implements TournamentEntityId<TournamentSubscription> {
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

	public static class Context implements TournamentEntityContext<TournamentSubscription> {
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
