package wisematches.playground.scribble.search.board;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import wisematches.personality.Personality;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.search.board.BoardsSearchEngine;
import wisematches.playground.search.board.LastMoveInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleSearchesEngine implements BoardsSearchEngine {
	private SessionFactory sessionFactory;

	private static final Collection<LastMoveInfo> EMPTY_SCRIBBLE_BOARD = Collections.emptyList();

	public ScribbleSearchesEngine() {
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<LastMoveInfo> findExpiringBoards() {
		final Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select new " + LastMoveInfo.class.getName() + "(board.boardId, board.gameSettings.daysPerMove, board.lastMoveTime) from " +
				ScribbleBoard.class.getName() + " board where board.gameResolution is null");
		List expiredBoards = query.list();
		if (expiredBoards.size() == 0) {
			return EMPTY_SCRIBBLE_BOARD;
		}
		return expiredBoards;
	}

	@Override
	public int getActiveBoardsCount(Personality personality) {
		final Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select count(*) from wisematches.playground.scribble.ScribbleBoard " +
				" board join board.playerHands hand where board.gameResolution is NULL and hand.playerId = ?");
		query.setParameter(0, personality.getId());
		return ((Number) query.uniqueResult()).intValue();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
