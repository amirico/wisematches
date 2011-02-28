package wisematches.server.gameplaying.scribble.board;

import wisematches.server.gameplaying.board.GameState;
import wisematches.server.gameplaying.room.search.BoardsSearchEngine;
import wisematches.server.gameplaying.room.search.ExpiringBoardInfo;
import wisematches.server.gameplaying.room.search.RatedBoardsInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleSearchesEngineBoards implements BoardsSearchEngine<ScribbleBoard> {
	private final ScribbleBoardDao scribbleBoardDao;

	private static final Collection<ExpiringBoardInfo> EMPTY_SCRIBBLE_BOARD = Collections.emptyList();

	public ScribbleSearchesEngineBoards(ScribbleBoardDao scribbleBoardDao) {
		this.scribbleBoardDao = scribbleBoardDao;
	}

	@Override
	public Collection<ExpiringBoardInfo> findExpiringBoards() {
		final Collection<ExpiringBoardInfo> expiredBoards = scribbleBoardDao.findExpiringBoards();
		if (expiredBoards.size() == 0) {
			return EMPTY_SCRIBBLE_BOARD;
		}
		return expiredBoards;
	}

	@Override
	public RatedBoardsInfo getRatedBoards(long playerId, Date startDate, Date endDate) {
		return scribbleBoardDao.getRatedBoards(playerId, startDate, endDate);
	}

	@Override
	public int getGamesCount(EnumSet<GameState> states) {
		return scribbleBoardDao.getGamesCount(states);
	}
}
