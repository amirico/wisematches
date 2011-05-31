package wisematches.playground.tracking;

import wisematches.personality.Personality;
import wisematches.playground.GameBoard;
import wisematches.playground.GamePlayerHand;
import wisematches.playground.GameSettings;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerTrackingCenter {
	void addStatisticListener(StatisticsListener l);

	void removeStatisticListener(StatisticsListener l);


	/**
	 * Returns current player's rating.
	 *
	 * @param person the player who's rating must be returned.
	 * @return the player's rating.
	 * @throws IllegalArgumentException if specified personality out of this manager.
	 */
	short getRating(Personality person);

	/**
	 * Returns statistic for specified player.
	 *
	 * @param personality the player id.
	 * @return the player statistic.
	 */
	Statistics getPlayerStatistic(Personality personality);

	/**
	 * Returns collection of all changes for specified board.
	 *
	 * @param board the board
	 * @return collection of rating changes or null if board doesn't exist or not finished yet.
	 */
	RatingChanges getRatingChanges(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board);

	/**
	 * Forecast possible rating changes if board will be closed right now. The method takes into account
	 * current player's rating and current board's state.
	 * <p/>
	 * The method can return different result for the same board and the same player because player's ratung
	 * can be changed or board's state can be changed.
	 * <p/>
	 * If game was finished please use {@link #getRatingChanges(wisematches.playground.GameBoard)} method instead.
	 * This method is based on current player's rating.
	 *
	 * @param board the board
	 * @return the forecasted rating changes
	 */
	RatingChanges forecastRatingChanges(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board);

	/**
	 * Returns rating curve container that contains information about all rating changes for specified player
	 * for specified dates with specified resolution.
	 *
	 * @param player	 the player who's curve should be returned.
	 * @param resolution the curve resolution. Indicates how many days must be grouped for one point. It's not possible
	 *                   to get curve with resolution less that one day at this moment.
	 * @param startDate  startDate date. If null all range will be used.
	 * @param endDate	endDate date. If null today will be used
	 * @return the rating curve.
	 * @throws IllegalArgumentException if resolution if zero or negative.
	 * @throws NullPointerException	 if {@code player} is null
	 */
	RatingChangesCurve getRatingChangesCurve(Personality player, int resolution, Date startDate, Date endDate);
}
