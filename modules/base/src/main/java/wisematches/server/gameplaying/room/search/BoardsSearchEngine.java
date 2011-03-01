package wisematches.server.gameplaying.room.search;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameState;

import java.util.Collection;
import java.util.EnumSet;

/**
 * This interface is searches engine for game board. It contains set of methods
 * that allow you search boards by some criterias.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface BoardsSearchEngine<B extends GameBoard<?, ?>> {
	/**
	 * Searches boards which is active now and can be expired. This methods returns
	 * only games with {@code IN_PROGRESS} state.
	 *
	 * @return the boards which is active now and can be expired.
	 */
	Collection<ExpiringBoard> findExpiringBoards();

	/**
	 * Returns count of games with specified states.
	 *
	 * @param states the set of states
	 * @return the count of games with specified states.
	 */
	int getGamesCount(EnumSet<GameState> states);
}