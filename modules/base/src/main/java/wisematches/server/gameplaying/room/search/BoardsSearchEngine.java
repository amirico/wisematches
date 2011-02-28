package wisematches.server.gameplaying.room.search;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameState;

import java.util.Collection;
import java.util.Date;
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
	Collection<ExpiringBoardInfo> findExpiringBoards();

	/**
	 * Returns information about rated board for this manager. Returned structure contains information about
	 * finished games from specified {@code startDate} to specified {@code endDate} that include
	 * board id, time when it was finished and changed after the game ration.
	 *
	 * @param playerId  the player id whose rating should be returned.
	 * @param startDate the start date or {@code null} if history from registration date should be returned.
	 * @param endDate   the end date or {@code null} if history to this moment should be returned.
	 * @return the rating history.
	 * @throws IllegalArgumentException if {@code endDate} is less or equals to {@code startDate}.
	 */
	RatedBoardsInfo getRatedBoards(long playerId, Date startDate, Date endDate);

	/**
	 * Returns count of games with specified states.
	 *
	 * @param states the set of states
	 * @return the count of games with specified states.
	 */
	int getGamesCount(EnumSet<GameState> states);
}