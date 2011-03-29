package wisematches.server.gameplaying.board;

/**
 * State of the game.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum GameState {
	/**
	 * Game in progress now
	 */
	ACTIVE,

	/**
	 * Game was finished and someone has won
	 */
	FINISHED,

	/**
	 * Game wasn't finished but was canceled.
	 */
	INTERRUPTED,
}
