package wisematches.playground.search.board;

import wisematches.personality.Personality;

import java.util.Collection;

/**
 * This interface is searches engine for game board. It contains set of methods
 * that allow you search boards by some criterias.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface BoardsSearchEngine {
	/**
	 * Searches boards which is active now and can be expired. This methods returns
	 * only games with {@code IN_PROGRESS} state.
	 *
	 * @return the boards which is active now and can be expired.
	 */
	Collection<LastMoveInfo> findExpiringBoards();

	/**
	 * Returns active games count for specified person.
	 *
	 * @param personality the person who games count should be calculated
	 * @return the number of games count.
	 */
	int getActiveBoardsCount(Personality personality);
}