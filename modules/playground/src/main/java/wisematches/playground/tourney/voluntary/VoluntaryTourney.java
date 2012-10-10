package wisematches.playground.tourney.voluntary;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;

import java.util.EnumSet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface VoluntaryTourney extends VoluntaryTourneyEntity<VoluntaryTourney, VoluntaryTourney.Id, VoluntaryTourney.Context> {
	String getTitle();

	int getPlayersCount();

	Language getLanguage();

	public final static class Id extends TourneyEntity.Id<VoluntaryTourney, Id> {
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

	public final static class Context extends TourneyEntity.Context<VoluntaryTourney, Context> {
		public Context() {
		}

		public Context(EnumSet<State> states) {
			super(states);
		}
	}
}
