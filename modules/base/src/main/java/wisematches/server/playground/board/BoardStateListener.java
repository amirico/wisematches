package wisematches.server.playground.board;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardStateListener extends GameBoardListener {
	<S extends GameSettings, P extends GamePlayerHand> void gameStarted(GameBoard<S, P> board);
}
