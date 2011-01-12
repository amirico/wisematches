package wisematches.server.web.modules.app.playboard;

import wisematches.server.games.board.GameBoard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class OpenBoardsBeanImpl implements OpenBoardsBean {
	private final Collection<GameBoard> gameBoards = new ArrayList<GameBoard>();

	public void addOpenedBoard(GameBoard board) {
		if (board == null) {
			throw new NullPointerException("Board can't be null");
		}
		gameBoards.add(board);
	}

	public void removeOpenedBoard(GameBoard board) {
		if (board == null) {
			throw new NullPointerException("Board can't be null");
		}
		gameBoards.remove(board);
	}

	public GameBoard getOpenedBoard(long id) {
		for (GameBoard gameBoard : gameBoards) {
			if (gameBoard.getBoardId() == id) {
				return gameBoard;
			}
		}
		return null;
	}

	public GameBoard closeOpenedBoard(long id) {
		final GameBoard board = getOpenedBoard(id);
		if (board != null) {
			removeOpenedBoard(board);
		}
		return board;
	}

	public Collection<GameBoard> getOpenedBoards() {
		return Collections.unmodifiableCollection(gameBoards);
	}
}
