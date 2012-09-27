package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;

import javax.persistence.Embeddable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyGame extends RegularTourneyEntity<TourneyGame, TourneyGame.Id, TourneyGame.Context> {
	int getGameId();

	TourneyGroup getTourneyGroup();

	@Embeddable
	public final class Id implements TourneyEntity.Id<TourneyGame, Id> {
		private TourneyGroup.Id id;
		private long gameId;

		private Id() {
		}

		public Id(TourneyGroup.Id id, long gameId) {
			this.id = id;
			this.gameId = gameId;
		}

		public long getGameId() {
			return gameId;
		}

		public TourneyGroup.Id getGroupId() {
			return id;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Id id1 = (Id) o;

			if (gameId != id1.gameId) return false;
			if (!id.equals(id1.id)) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = id.hashCode();
			result = 31 * result + (int) (gameId ^ (gameId >>> 32));
			return result;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("Id");
			sb.append("{id=").append(id);
			sb.append(", gameId=").append(gameId);
			sb.append('}');
			return sb.toString();
		}
	}

	public final class Context implements TourneyEntity.Context<TourneyGame, Context> {
		private final int tournament;
		private final Language language;
		private final TourneySection section;

		public Context(int tournament, Language language, TourneySection section) {
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

		public TourneySection getSection() {
			return section;
		}
	}
}
