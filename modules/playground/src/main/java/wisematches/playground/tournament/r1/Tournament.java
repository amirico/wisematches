package wisematches.playground.tournament.r1;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Tournament {
	/**
	 * Returns number of the tournament.
	 *
	 * @return number of the tournament.
	 */
	int getNumber();

	/**
	 * Returns only date when tournament was started (time always 00:00:00.000)
	 *
	 * @return only date when tournament was started  (time always 00:00:00.000)
	 */
	Date getStartedDate();

	/**
	 * Returns date and time when tournament has been finished. If tournament is not finished
	 * {@code null} will be returned.
	 *
	 * @return date and time when tournament has been finished or {@code null} if it's not finished yet.
	 */
	Date getFinishedData();
}
