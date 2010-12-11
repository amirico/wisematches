package wisematches.server.core.board;

/**
 * State of the game.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum GameState {
    /**
     * Game isn't ready for play and waiting new players.
     */
    WAITING,

    /**
     * Game in progress now
     */
    IN_PROGRESS,

    /**
     * Game was finished and someone has won
     */
    FINISHED,

    /**
     * Game was finished and no one has won
     */
    DRAW,

    /**
     * Game wasn't finished but was canceled.
     */
    INTERRUPTED,
}
