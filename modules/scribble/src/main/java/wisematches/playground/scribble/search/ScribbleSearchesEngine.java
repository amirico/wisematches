package wisematches.playground.scribble.search;

import wisematches.playground.GameResolution;
import wisematches.playground.scribble.ScribbleBoardDao;
import wisematches.playground.search.BoardsSearchEngine;
import wisematches.playground.search.LastMoveInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleSearchesEngine implements BoardsSearchEngine {
	private ScribbleBoardDao scribbleBoardDao;

	private static final Collection<LastMoveInfo> EMPTY_SCRIBBLE_BOARD = Collections.emptyList();

	public ScribbleSearchesEngine() {
	}

	@Override
	public Collection<LastMoveInfo> findExpiringBoards() {
		final Collection<LastMoveInfo> expiredBoards = scribbleBoardDao.findExpiringBoards();
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
