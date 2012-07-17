package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.util.Date;

/**
 * The tournament group is last tournament entity that describes players and games for one group.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class TournamentGroup extends TournamentEntity {
	protected TournamentGroup() {
	}

	/**
	 * Returns number of the group.
	 *
	 * @return the number of the group.
	 */
	public abstract int getGroup();

	/**
	 * Returns round fot the group.
	 *
	 * @return the round fot the group.
	 */
	public abstract int getRound();

	/**
	 * Returns tournament for the group.
	 *
	 * @return the tournament for the group.
	 */
	public abstract int getTournament();

	/**
	 * Returns language for this group.
	 *
	 * @return the group's language.
	 */
	public abstract Language getLanguage();

	/**
	 * The section for this group.
	 *
	 * @return the group's section.
	 */
	public abstract TournamentSection getSection();


	/**
	 * Returns date when the group was started. Never {@code null}.
	 *
	 * @return returns date when the group was started.
	 */
	public abstract Date getStartedDate();

	/**
	 * Returns date when group was finished or {@code null} is group is not finished yet.
	 *
	 * @return the date when group was finished or {@code null} if it's still active.
	 */
	public abstract Date getFinishedDate();

	/**
	 * Returns number of total games in the group.
	 *
	 * @return the number of total games in the group.
	 */
	public abstract int getTotalGamesCount();

	/**
	 * Returns number of finished games in the group.
	 *
	 * @return the number of finished games in the group.
	 */
	public abstract int getFinishedGamesCount();


	/**
	 * Returns array of all players in the group.
	 *
	 * @return the array of player in the group.
	 */
	public abstract long[] getPlayers();

	/**
	 * Returns game id for two specified players.
	 * <p/>
	 * Please note that method can returns two different games in case of {@code p1-p2} and {@code p2-p1}
	 *
	 * @param p1 the first player
	 * @param p2 the second player
	 * @return the game id or zero if players don't have common game.
	 * @throws IllegalArgumentException if any player doesn't belong to the group.
	 */
	public abstract long getGame(long p1, long p2);

	/**
	 * Returns current tournament's scores for the player
	 *
	 * @param player the player who's scores should be returned.
	 * @return the player's scores.
	 * @throws IllegalArgumentException if player doesn't belong to the group.
	 */
	public abstract short getScores(long player);

	/**
	 * Returns points for finished game for specified players. The method returns points
	 * for the game returned by {@link #getGame(long, long)} method.
	 * <p/>
	 * Please note that method can returns two different games in case of {@code p1-p2} and {@code p2-p1}
	 *
	 * @param p1 the first player
	 * @param p2 the second player
	 * @return the game's points
	 * @throws IllegalArgumentException if any player doesn't belong to the group.
	 */
	public abstract short getPoints(long p1, long p2);


	public static Id createId(int tournament, Language language, TournamentSection section, int round, int group) {
		return new Id(tournament, language, section, round, group);
	}

	public static Context createContext(int tournament, Language language, TournamentSection section, int round) {
		return new Context(tournament, language, section, round);
	}


	public static class Id implements TournamentEntityId<TournamentGroup> {
		private final int tournament;
		private final Language language;
		private final TournamentSection section;
		private final int round;
		private final int group;

		private Id(int tournament, Language language, TournamentSection section, int round, int group) {
			this.group = group;
			this.round = round;
			this.section = section;
			this.language = language;
			this.tournament = tournament;
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

		public int getGroup() {
			return group;
		}
	}

	public static class Context implements TournamentEntityContext<TournamentGroup> {
		private final int tournament;
		private final Language language;
		private final TournamentSection section;
		private final int round;

		private Context(int round, Language language, TournamentSection section, int tournament) {
			this.round = round;
			this.section = section;
			this.language = language;
			this.tournament = tournament;
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
}