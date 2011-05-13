package wisematches.tracking.rating;

import wisematches.server.personality.Personality;
import wisematches.server.playground.board.GameBoard;

/**
 * This listener is used in {@code RatingsManager} to notify clients about players ratings.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerRatingListener {
	/**
	 * This method is invoked when player's rating was changed after a game. Specified player already
	 * contains new rating.
	 *
	 * @param player	the player who's rating was changed
	 * @param gameBoard the finished board
	 * @param oldRating old rating
	 * @param newRating new rating
	 */
	void playerRatingChanged(Personality player, GameBoard gameBoard, short oldRating, short newRating);
}
