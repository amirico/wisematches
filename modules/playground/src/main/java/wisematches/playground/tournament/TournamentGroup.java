package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.util.Date;

/**
 * The tournament group is last tournament entity that describes players and games for one group.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentGroup extends TournamentEntity<TournamentGroup, TournamentGroup.Id, TournamentGroup.Context> {
	int getTournament();

	/**
	 * Returns language for this group.
	 *
	 * @return the group's language.
	 */
	Language getLanguage();

	/**
	 * The section for this group.
	 *
	 * @return the group's section.
	 */
	TournamentSection getSection();

	/**
	 * Returns round fot the group.
	 *
	 * @return the round fot the group.
	 */
	int getRound();

	/**
	 * Returns number of the group.
	 *
	 * @return the number of the group.
	 */
	int getGroup();

	Date getStartedDate();

	Date getFinishedDate();


	/**
	 * Returns number of total games in the group.
	 *
	 * @return the number of total games in the group.
	 */
	int getTotalGamesCount();

	/**
	 * Returns number of finished games in the group.
	 *
	 * @return the number of finished games in the group.
	 */
	int getFinishedGamesCount();


	/**
	 * Returns array of all players in the group.
	 *
	 * @return the array of player in the group.
	 */
	long[] getPlayers();

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
	long getGame(long p1, long p2);

	/**
	 * Returns current tournament's scores for the player
	 *
	 * @param player the player who's scores should be returned.
	 * @return the player's scores.
	 * @throws IllegalArgumentException if player doesn't belong to the group.
	 */
	short getScores(long player);

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
	short getPoints(long p1, long p2);


	public final class Id implements TournamentEntity.Id<TournamentGroup, Id, Context> {
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

	public final class Context implements TournamentEntity.Context<TournamentGroup, Id, Context> {
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