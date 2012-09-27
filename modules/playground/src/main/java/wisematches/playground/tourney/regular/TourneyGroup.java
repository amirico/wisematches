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
	TourneyRound getTourneyRound();


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