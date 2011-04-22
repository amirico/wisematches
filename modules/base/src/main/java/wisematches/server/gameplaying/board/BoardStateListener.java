package wisematches.server.gameplaying.board;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameBoardListener;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardStateListener extends GameBoardListener {
	void gameStarted(GameBoard board);
}
