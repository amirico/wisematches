package wisematches.server.games.scribble.board;

import wisematches.server.core.board.GameState;
import wisematches.server.core.room.ExpiringBoardInfo;
import wisematches.server.core.room.RatedBoardsInfo;
import wisematches.server.core.room.SearchesEngine;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleSearchesEngine implements SearchesEngine<ScribbleBoard> {
    private final ScribbleBoardDao scribbleBoardDao;

    private static final Collection<ExpiringBoardInfo> EMPTY_SCRIBBLE_BOARD = Collections.emptyList();

    public ScribbleSearchesEngine(ScribbleBoardDao scribbleBoardDao) {
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
