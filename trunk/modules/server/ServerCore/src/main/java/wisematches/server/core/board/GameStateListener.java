package wisematches.server.core.board;

/**
 * The listeners of the state a game.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameStateListener {
    /**
     * Indicates that game was started
     *
     * @param board      the board that was started.
     * @param playerTurn the player who has a turn.
     */
    void gameStarted(GameBoard board, GamePlayerHand playerTurn);

    /**
     * Game was finished and someone has won
     *
     * @param board     the board game that was finished
     * @param wonPlayer the player who won  @see wisematches.server.core.board.GameState#FINISHED
     */
    void gameFinished(GameBoard board, GamePlayerHand wonPlayer);

    /**
     * No one player has won.
     *
     * @param board the board game that was finished
     * @see wisematches.server.core.board.GameState#DRAW
     */
    void gameDraw(GameBoard board);

    /**
     * Game was interrupted by specified player.
     *
     * @param board             the interrupted board.
     * @param interrupterPlayer the interrupter.  @see wisematches.server.core.board.GameState#INTERRUPTED
     * @param byTimeout         indicates that game was interrupted by timeout.
     */
    void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout);
}
