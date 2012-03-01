package wisematches.playground.tournament.upcoming;

import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentSection;

/**
 * The tournament request is player's application for upcoming tournament. It contains
 * player, section and language.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentRequest {
	/**
	 * Returns player id who sent the request
	 *
	 * @return the player id who sent the request
	 */
	long getPlayer();

	/**
	 * Returns number of a tournament.
	 *
	 * @return the number of a tournament.
	 */
	int getAnnouncement();

	/**
	 * Returns tournament's language.
	 *
	 * @return the tournament's language.
	 */
	Language getLanguage();

	/**
	 * Returns tournament's section.
	 *
	 * @return the tournament's section.
	 */
	TournamentSection getSection();
}
