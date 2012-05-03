package wisematches.playground.tournament;

import java.util.Date;

/**
 * Base interface for tournament representation. Each tournament has unique number,
 * start date and finish date.
 * <p/>
 * Each tournament contains set of rounds and each round has set of groups.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Tournament {
	/**
	 * Returns number of the tournament.
	 *
	 * @return the number of the tournament.
	 */
	int getNumber();

	/**
	 * Returns start date of the tournament. If the date in the future when the tournament
	 * was announced but not started yet.
	 *
	 * @return the start date
	 */
	Date getStartDate();

	/**
	 * Returns finish date of the tournament. If date is null when tournament is not finished or not started yet.
	 *
	 * @return the finish date or {@code null} if the tournament isn't finished.
	 */
	Date getFinishDate();

	/**
	 * Indicates is the tournament started or not.
	 *
	 * @return {@code true} if tournament started; otherwise - {@code false}.
	 */
	boolean isStarted();

	/**
	 * Indicates is the tournament finished or not.
	 *
	 * @return {@code true} if tournament finished; otherwise - {@code false}.
	 */
	boolean isFinished();
}
