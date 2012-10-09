package wisematches.playground.tourney.regular;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneySubscription {
	long getPlayer();

	int getTourney();

	Language getLanguage();

	TourneySection getSection();
}