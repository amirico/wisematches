package wisematches.server.gameplaying.scribble.room.search;

import wisematches.server.gameplaying.board.GameResolution;
import wisematches.server.gameplaying.room.search.BoardLastMoveInfo;
import wisematches.server.gameplaying.room.search.BoardsSearchEngine;
import wisematches.server.gameplaying.scribble.board.ScribbleBoardDao;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleSearchesEngine implements BoardsSearchEngine {
	private ScribbleBoardDao scribbleBoardDao;

	private static final Collection<BoardLastMoveInfo> EMPTY_SCRIBBLE_BOARD = Collections.emptyList();

	public ScribbleSearchesEngine() {
	}

	@Override
	public Collection<BoardLastMoveInfo> findExpiringBoards() {
		final Collection<BoardLastMoveInfo> expiredBoards = scribbleBoardDao.findExpiringBoards();
		if (expiredBoards.size() == 0) {
			return EMPTY_SCRIBBLE_BOARD;
		}
		return expiredBoards;
	}
/*

	@Override
	public RatedBoardsInfo getRatedBoards(long playerId, Date startDate, Date endDate) {
		return scribbleBoardDao.getRatedBoards(playerId, startDate, endDate);
	}
*/

	@Override
	public int getGamesCount(EnumSet<GameResolution> resolutions) {
		return scribbleBoardDao.getGamesCount(resolutions);
	}

	public void setScribbleBoardDao(ScribbleBoardDao scribbleBoardDao) {
		this.scribbleBoardDao = scribbleBoardDao;
	}
}
