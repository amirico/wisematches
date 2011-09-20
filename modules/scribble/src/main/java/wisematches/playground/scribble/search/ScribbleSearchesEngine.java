package wisematches.playground.scribble.search;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import wisematches.personality.Personality;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.search.player.BoardsSearchEngine;
import wisematches.playground.search.player.LastMoveInfo;

import java.util.Collection;
import java.util.Collections;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleSearchesEngine implements BoardsSearchEngine {
	private HibernateTemplate hibernateTemplate;

	private static final Collection<LastMoveInfo> EMPTY_SCRIBBLE_BOARD = Collections.emptyList();

	public ScribbleSearchesEngine() {
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<LastMoveInfo> findExpiringBoards() {
		final Collection<LastMoveInfo> expiredBoards = hibernateTemplate.find(
				"select new " + LastMoveInfo.class.getName() + "(board.boardId, board.gameSettings.daysPerMove, board.lastMoveTime) from " +
						ScribbleBoard.class.getName() + " board where board.gameResolution is null");
		if (expiredBoards.size() == 0) {
			return EMPTY_SCRIBBLE_BOARD;
		}
		return expiredBoards;
	}

	@Override
	public int getActiveBoardsCount(Personality personality) {
		return DataAccessUtils.intResult(hibernateTemplate.find("select count(*) from wisematches.playground.scribble.ScribbleBoard " +
				" board join board.playerHands hand where board.gameResolution is NULL and hand.playerId = ?", personality.getId()));
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
}
