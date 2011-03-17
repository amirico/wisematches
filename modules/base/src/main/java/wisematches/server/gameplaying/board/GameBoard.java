package wisematches.server.gameplaying.board;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface GameBoard<S extends GameSettings, P extends GamePlayerHand> {
	void addGameBoardListener(GameBoardListener listener);

	void removeGameBoardListener(GameBoardListener listener);

	/**
	 * Returns id of this board.
	 *
	 * @return the id of this board.
	 */
	long getBoardId();

	/**
	 * Returns time when game was started.
	 *
	 * @return return time when game was started or {@code null} if game was not started yet.
	 */
	Date getStartedTime();

	/**
	 * Returns time when game was finished.
	 *
	 * @return return time when game was finished or {@code null} if game is not finished yet.
	 */
	Date getFinishedTime();

	/**
	 * Return time of last move in milliseconds.
	 *
	 * @return the time of last move in milliseconds.
	 */
	Date getLastMoveTime();

	/**
	 * Indicates should this game be rated after finished.
	 * <p/>
	 * Any game can be created as no rated or this flag can be enabled if game was terminated and should not be rated.
	 *
	 * @return {@code true} is game should be rated; {@code false} - otherwise.
	 */
	boolean isRatedGame();

	/**
	 * Returns settings of this game.
	 *
	 * @return the settings of this game.
	 */
	S getGameSettings();

	/**
	 * Returns player that has a turn.
	 * <p/>
	 * If game was terminated this method return player who terminate this game.
	 * <p/>
	 * If game was finished correct method returns {@code null}
	 *
	 * @return the active player, player who has terminate a game or {@code null} if game has finished correct.
	 */
	P getPlayerTurn();

	/**
	 * Makes move for active player and returns points for this turn.
	 *
	 * @param move the move.
	 * @return points the appropriate game move object
	 * @throws GameMoveException if move can't be done
	 */
	GameMove makeMove(PlayerMove move) throws GameMoveException;

	/**
	 * Returns unmodifiable collection of maden moves.
	 *
	 * @return the unmodifiable collection of maden moves.
	 */
	List<GameMove> getGameMoves();

	/**
	 * Returns player's hand by player's id.
	 *
	 * @param playerId the player id who hand should be returned.
	 * @return the player's hand.
	 */
	P getPlayerHand(long playerId);

	/**
	 * Returns unmodifiable collection of all players in this game.
	 *
	 * @return the list of all players on this board.
	 */
	List<P> getPlayersHands();

	/**
	 * Returns won player for this game. This method throws {@code java.lang.IllegalStateException}
	 * if game is not finished yet.
	 *
	 * @return the won player or {@code null} if game was finished with draw.
	 * @throws IllegalStateException if game is not finished yet (has state {@code GameState.IN_PROGRESS}
	 *                               or {@code GameState.WAITING}.
	 */
	P getWonPlayer();

	/**
	 * Returns state of this game.
	 *
	 * @return the game's state
	 */
	GameState getGameState();

	/**
	 * Terminates this game. This method is used to terminate game by timeout.
	 *
	 * @throws UnsuitablePlayerException if specified player doesn't belong to this game.
	 * @throws GameNotReadyException	 if game wasn't created yet.
	 * @throws GameFinishedException	 if game already finished.
	 * @throws IllegalStateException	 if timeout is not expired by this method was called.
	 * @see GameState#INTERRUPTED
	 */
	public void terminate() throws GameMoveException;

	/**
	 * Closes this game. State changed to interrupted.
	 *
	 * @param player the player who closed the game
	 * @throws UnsuitablePlayerException if specified player doesn't belong to this game.
	 * @throws GameNotReadyException	 if game wasn't created yet.
	 * @throws GameFinishedException	 if game already finished.
	 * @see GameState#INTERRUPTED
	 */
	void close(P player) throws GameMoveException;
}
