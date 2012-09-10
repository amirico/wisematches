package wisematches.playground.tourney;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyElement<
		T extends TourneyEntity<T, I, C>,
		I extends TourneyEntity.Id<T, I>,
		C extends TourneyEntity.Context<T, C>> extends TourneyEntity<T, I, C> {
	/**
	 * Returns started date for this tournament. Date can be null if the tournament is not started yet.
	 *
	 * @return started date for this tournament. Date can be null if the tournament is not started yet.
	 */
	Date getStartedDate();

	/**
	 * Returns date when the tournament was finished or null if it's not finished yet.
	 *
	 * @return the date when the tournament was finished or null if it's not finished yet.
	 */
	Date getFinishedDate();


	/**
	 * @author Sergey Klimenko (smklimenko@gmail.com)
	 */
	enum State {
		STARTED,

		FINISHED,

		SCHEDULED
	}
}
