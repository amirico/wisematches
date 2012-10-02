package wisematches.playground.tourney.regular;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneySubscriptions {
	int getTourney();

	int getPlayers(Language language);

	int getPlayers(Language language, TourneySection section);
}
