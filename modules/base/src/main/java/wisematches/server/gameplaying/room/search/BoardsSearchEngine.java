package wisematches.server.gameplaying.room.search;

import wisematches.server.gameplaying.board.GameResolution;

import java.util.Collection;
import java.util.EnumSet;

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
	Collection<BoardLastMoveInfo> findExpiringBoards();

	/**
	 * Returns count of games with specified resolution. If specified resolutions set is null when
	 * number of active games will be returned.
	 *
	 * @param resolutions the set of states or null to get number of active games.
	 * @return the count of games with specified states.
	 */
	int getGamesCount(EnumSet<GameResolution> resolutions);
}