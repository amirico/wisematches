package wisematches.playground.tourney.regular;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneySubscription {
	int getTourney();

	long getPlayer();

	Language getLanguage();

	TourneySection geTourneySection();
}