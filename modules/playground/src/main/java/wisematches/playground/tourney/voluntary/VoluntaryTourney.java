package wisematches.playground.tourney.voluntary;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntity;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface VoluntaryTourney extends VoluntaryTourneyElement<VoluntaryTourney, VoluntaryTourney.Id, VoluntaryTourney.Context> {
	String getTitle();

	int getPlayersCount();

	Language getLanguage();

	public final static class Id implements TourneyEntity.Id<VoluntaryTourney, Id> {
	}

	public final static class Context implements TourneyEntity.Context<VoluntaryTourney, Context> {
	}
}
