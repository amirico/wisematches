package wisematches.playground;

/**
 * <code>GamePlayerHand</code> is a hand of the player. It contains information about player on the board, like
 * it's point, it's items in hand and so on.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GamePlayerHand {
	/**
	 * Returns points of the player on the board.
	 *
	 * @return the player's points
	 */
	short getPoints();

	/**
	 * Indicates that this player is game winner.
	 *
	 * @return {@code true} if player is winner; {@code false} - otherwise.
	 */
	boolean isWinner();

	/**
	 * Returns old player's rating.
	 *
	 * @return the old player's rating.
	 */
	short getOldRating();

	/**
	 * Returns new player's rating after the game.
	 *
	 * @return new player's rating after the game.
	 */
	short getNewRating();
}
