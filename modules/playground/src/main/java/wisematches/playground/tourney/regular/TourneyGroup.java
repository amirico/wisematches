package wisematches.playground.tourney.regular;

import wisematches.core.Language;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneyGameResolution;

import java.util.EnumSet;

/**
 * The tourney group is last tourney entity that describes players and games for one group.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyGroup extends RegularTourneyEntity<TourneyGroup, TourneyGroup.Id, TourneyGroup.Context> {
	/**
	 * Returns group number
	 *
	 * @return the group number.
	 */
	int getGroup();

	/**
	 * Returns roundId that this group is belong to.
	 *
	 * @return the tourney roundId.
	 */
	TourneyRound getRound();


	/**
	 * All players in this group.
	 *
	 * @return array of all players in this group.
	 */
	long[] getPlayers();

	/**
	 * Returns total players count in this group.
	 *
	 * @return the total players count in this group.
	 */
	int getPlayersCount();

	/**
	 * Returns total player scores
	 *
	 * @param player the player who's scores must be returned.
	 * @return scores object.
	 */
	int getPlayerScores(long player);

	/**
	 * Returns game result between two specified players. Order of players is important.
	 *
	 * @param player   player who's success will be returned.
	 * @param opponent opponent.
	 * @return the game result that is played by specified players or {@code null} if game is not finished.
	 * @throws IllegalArgumentException if any player doesn't belong to this group.
	 */
	TourneyGameResolution getPlayerSuccess(long player, long opponent);


	/**
	 * Returns count of games in this group.
	 *
	 * @return the count of games in this group.
	 */
	int getTotalGamesCount();

	/**
	 * Returns count of finished games in this group.
	 *
	 * @return the count of finished games in this group.
	 */
	int getFinishedGamesCount();

	/**
	 * Returns game id between two specified players. Order of players is not important.
	 *
	 * @param player   first player
	 * @param opponent second player.
	 * @return the game id that is played by specified players.
	 * @throws IllegalArgumentException if any player doesn't belong to this group.
	 */
	long getGameId(long player, long opponent);


	public final class Id extends TourneyEntity.Id<TourneyGroup, Id> {
		private TourneyRound.Id id;
		private int group;

		private Id() {
		}

		public Id(int tourney, Language language, TourneySection section, int round, int group) {
			this(new TourneyRound.Id(tourney, language, section, round), group);
		}

		public Id(TourneyRound.Id id, int group) {
			this.id = id;
			this.group = group;
		}

		public int getGroup() {
			return group;
		}

		public TourneyRound.Id getRoundId() {
			return id;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Id id1 = (Id) o;
			return group == id1.group && id.equals(id1.id);
		}

		@Override
		public int hashCode() {
			int result = id.hashCode();
			result = 31 * result + group;
			return result;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Id");
			sb.append("{id=").append(id);
			sb.append(", group=").append(group);
			sb.append('}');
			return sb.toString();
		}
	}

	public final class Context extends TourneyEntity.Context<TourneyGroup, Context> {
		private TourneyRound.Id roundId;

		public Context(EnumSet<State> states) {
			super(states);
		}

		public Context(TourneyRound.Id roundId, EnumSet<State> states) {
			super(states);
			if (roundId == null) {
				throw new NullPointerException("Round id can't be null");
			}
			this.roundId = roundId;
		}

		public TourneyRound.Id getRoundId() {
			return roundId;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Context");
			sb.append(", roundId=").append(roundId);
			sb.append('}');
			return sb.toString();
		}
	}
}