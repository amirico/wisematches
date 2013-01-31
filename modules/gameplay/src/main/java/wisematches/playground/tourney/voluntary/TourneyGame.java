package wisematches.playground.tourney.voluntary;

import wisematches.playground.tourney.TourneyEntity;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyGame extends VoluntaryTourneyEntity<TourneyGame, TourneyGame.Id, TourneyGame.Context> {
	public final static class Id extends TourneyEntity.Id<TourneyGame, Id> {
		@Override
		public int hashCode() {
			throw new UnsupportedOperationException("TODO: Not implemented");
		}

		@Override
		public boolean equals(Object obj) {
			throw new UnsupportedOperationException("TODO: Not implemented");
		}

		@Override
		public String toString() {
			throw new UnsupportedOperationException("TODO: Not implemented");
		}
	}

	public final static class Context extends TourneyEntity.Context<TourneyGame, Context> {
		public Context() {
		}

		public Context(EnumSet<State> states) {
			super(states);
		}
	}
}
