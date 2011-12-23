package wisematches.playground.tournament;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentPoster {
	/**
	 * Returns number of next tournament.
	 *
	 * @return the number of next tournament.
	 */
	int getNumber();

	/**
	 * Returns date for next scheduled tournament.
	 *
	 * @return the date for next scheduled tournament.
	 */
	Date getScheduledDate();

	/**
	 * Indicates is the tournament for the poster has been started or not.
	 *
	 * @return {@code true} if tournament has been started; {@code false} - otherwise.
	 */
	boolean isStarted();
}
