package wisematches.server.gameplaying.board;

/**
 * This event is fired by {@code GameBoard} using {@code GameMoveListener} to indicate that move was maden or passed.
 * <p/>
 * This event contains information about move: board, player and so on.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class GameMoveEvent {
	private final GameBoard gameBoard;
	private final GamePlayerHand player;
	private final GameMove gameMove;
	private final GamePlayerHand nextPlayer;

	/**
	 * Create new maden move event.
	 *
	 * @param gameBoard  the board
	 * @param player	 the player who made move
	 * @param nextPlayer the player who can move next. Can be {@code null} if no next player (game finished).
	 * @param gameMove   the game move
	 * @throws NullPointerException if any parameter except {@code nextPlayer} is null.
	 */
	public GameMoveEvent(GameBoard gameBoard, GamePlayerHand player, GameMove gameMove, GamePlayerHand nextPlayer) {
		if (gameBoard == null) {
			throw new NullPointerException("Game boar is null");
		}
		if (player == null) {
			throw new NullPointerException("Player is null");
		}
		if (gameMove == null) {
			throw new NullPointerException("Game move is null");
		}

		this.gameBoard = gameBoard;
		this.player = player;
		this.nextPlayer = nextPlayer;
		this.gameMove = gameMove;
	}

	/**
	 * Returns associated board.
	 *
	 * @return the game board.
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}

	/**
	 * Return player who made a move.
	 *
	 * @return the player who made a move.
	 */
	public GamePlayerHand getPlayer() {
		return player;
	}

	/**
	 * Returns player who move next.
	 *
	 * @return the next move player or {@code null} if there is no next player (game was finished).
	 */
	public GamePlayerHand getNextPlayer() {
		return nextPlayer;
	}

	/**
	 * Returns done move or {@code null} if turn was passed.
	 *
	 * @return done move or {@code null} if turn was passed.
	 */
	public GameMove getGameMove() {
		return gameMove;
	}
}
