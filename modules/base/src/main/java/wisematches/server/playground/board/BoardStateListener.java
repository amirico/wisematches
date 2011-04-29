package wisematches.server.playground.board;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardStateListener extends GameBoardListener {
	void gameStarted(GameBoard board);
}
