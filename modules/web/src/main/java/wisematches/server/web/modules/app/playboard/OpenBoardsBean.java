package wisematches.server.web.modules.app.playboard;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.utils.sessions.ImplementationBean;

import java.util.Collection;

/**
 * Player Session Bean that contains collection of opened board for player. This bean has a
 * methods for manipulation of opened boards.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@ImplementationBean(OpenBoardsBeanImpl.class)
public interface OpenBoardsBean {
	/**
	 * Adds specified board to this bean.
	 * <p/>
	 * If bean already contains specified board board will be added twice.
	 *
	 * @param board the board to be added.
	 * @throws NullPointerException if board is {@code null}
	 */
	void addOpenedBoard(GameBoard board);

	/**
	 * Removes specified board from this bean. If board is unknown nothing will be happend.
	 *
	 * @param board the board to be removed.
	 * @throws NullPointerException if board is {@code null}
	 */
	void removeOpenedBoard(GameBoard board);

	/**
	 * Returns opened board by specified id.
	 *
	 * @param id the board's id
	 * @return board by specified id or {@code null} if board with specified id is not opened.
	 */
	GameBoard getOpenedBoard(long id);

	/**
	 * Close board with specified id. This operation is equivalent of
	 * two operations: {@code getOpenedBoard } and when {@code removeOpenedBoard}.
	 *
	 * @param id the board to be closed or {@code null} if board with specified id is unknown.
	 * @return the board's id.
	 */
	GameBoard closeOpenedBoard(long id);

	/**
	 * Returns unmodifiable collection of all opened boards.
	 *
	 * @return unmodifiable collection of all opened boards.
	 */
	Collection<GameBoard> getOpenedBoards();
}
