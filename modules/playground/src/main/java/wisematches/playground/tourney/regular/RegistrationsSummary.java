package wisematches.playground.tourney.regular;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegistrationsSummary {
	int getTourney();

	int getPlayers();

	int getPlayers(Language language);

	int getPlayers(Language language, TourneySection section);

	boolean hasPlayers(Language language, TourneySection section);
}
