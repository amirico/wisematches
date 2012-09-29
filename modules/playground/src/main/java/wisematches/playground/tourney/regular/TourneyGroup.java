package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;

import javax.persistence.Embeddable;

/**
 * The tournament group is last tournament entity that describes players and games for one group.
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
	 * Returns round that this group is belong to.
	 *
	 * @return the tourney round.
	 */
	TourneyRound getRound();

	/**
	 * Returns all games which take part in this group. The array contains first all games for first player,
	 * when for second player and so on.
	 *
	 * @return all games which take part in this group.
	 */
	long[] getGames();

	/**
	 * All players in this group.
	 *
	 * @return array of all players in this group.
	 */
	long[] getPlayers();

	/**
	 * Returns scores for each player in this group. Index in this array is equals to player's index in {@link #getPlayers()} array.
	 *
	 * @return all scores for players in this group.
	 */
	short[] getScores();

	/**
	 * Returns scores for specified player only.
	 *
	 * @param player the player id
	 * @return player's scores.
	 */
	short getScores(long player);

	/**
	 * Returns game id between two specified players. Order of players are not important.
	 *
	 * @param p1 first player
	 * @param p2 second player.
	 * @return the game id that is played by specified players.
	 * @throws IllegalArgumentException if any player doesn't belong to this group.
	 */
	long getGameId(long p1, long p2);

	@Embeddable
	public final class Id implements TourneyEntity.Id<TourneyGroup, Id> {
		private TourneyRound.Id id;
		private int group;

		private Id() {
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

	public final class Context implements TourneyEntity.Context<TourneyGroup, Context> {
		private final int tournament;
		private final Language language;
		private final TourneySection section;
		private final int round;

		public Context(int round, Language language, TourneySection section, int tournament) {
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

		public TourneySection getSection() {
			return section;
		}

		public int getRound() {
			return round;
		}
	}
}