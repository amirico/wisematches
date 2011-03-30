package wisematches.server.gameplaying.scribble.room.search;

import wisematches.server.gameplaying.board.GameResolution;
import wisematches.server.gameplaying.room.search.BoardsSearchEngine;
import wisematches.server.gameplaying.room.search.ExpiringBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleBoardDao;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleSearchesEngine implements BoardsSearchEngine<ScribbleBoard> {
	private ScribbleBoardDao scribbleBoardDao;

	private static final Collection<ExpiringBoard> EMPTY_SCRIBBLE_BOARD = Collections.emptyList();

	public ScribbleSearchesEngine() {
	}

	@Override
	public Collection<ExpiringBoard> findExpiringBoards() {
		final Collection<ExpiringBoard> expiredBoards = scribbleBoardDao.findExpiringBoards();
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
