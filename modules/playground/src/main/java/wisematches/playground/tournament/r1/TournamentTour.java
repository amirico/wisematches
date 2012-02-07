package wisematches.playground.tournament.r1;

import wisematches.personality.Language;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentTour {
	/**
	 * Returns number of the tour in the tournament.
	 *
	 * @return the number of the tour in the tournament.
	 */
	int getNumber();

	/**
	 * Returns tournament that the tour belongs to.
	 *
	 * @return the tournament that the tour belongs to.
	 */
	int getTournament();

	/**
	 * Returns date when the tour was started.
	 *
	 * @return the date when the tour was started.
	 */
	Date getStartedDate();

	/**
	 * Returns date when the tour was finished or {@code null} if tour is active
	 *
	 * @return the date when the tour was finished or {@code null} if tour is active.
	 */
	Date getFinishedDate();

	/**
	 * Returns language of the tour.
	 *
	 * @return the language of the tour.
	 */
	Language getLanguage();

	/**
	 * Returns section for the tour.
	 *
	 * @return the section for the tour.
	 */
	TournamentSection getSection();
}
