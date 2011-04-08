package wisematches.server.gameplaying.scribble.board;

import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import wisematches.server.gameplaying.room.search.BoardLastMoveInfo;
import wisematches.server.personality.Personality;

import java.util.Collection;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleJPABoardDao extends JpaDaoSupport {
	public ScribbleJPABoardDao() {
	}

	public ScribbleBoard getScribbleBoard(long boardId) {
		final JpaTemplate jpaTemplate = getJpaTemplate();
		ScribbleBoard scribbleBoard = jpaTemplate.find(ScribbleBoard.class, boardId);
		jpaTemplate.getEntityManager().detach(scribbleBoard);
		return scribbleBoard;

//		final HibernateTemplate template = getHibernateTemplate();
//		return template.get(ScribbleBoard.class, boardId);
	}

	public void saveScribbleBoard(ScribbleBoard scribbleBoard) {
		final JpaTemplate jpaTemplate = getJpaTemplate();

		// We call merge object because original can't be associated with another session
//		if (scribbleBoard.getBoardId() == 0) {
		jpaTemplate.persist(scribbleBoard);
		jpaTemplate.flush();
//		} else {
//			template.u(scribbleBoard);
//		}
//		template.flush();
	}

	@SuppressWarnings("unchecked")
	public Collection<Long> getActiveBoards(Personality player) {
		final JpaTemplate template = getJpaTemplate();
		return (List<Long>) template.find("select board.boardId from wisematches.server.gameplaying.scribble.board.ScribbleBoard " +
				" board join board.playersIterator.playerHands hands where board.gameResolution is NULL and hands.playerId = ?", player.getId());
	}

	@SuppressWarnings("unchecked")
	public Collection<BoardLastMoveInfo> findExpiringBoards() {
		final JpaTemplate template = getJpaTemplate();
		return template.find("select new " + BoardLastMoveInfo.class.getName() + "(board.boardId, board.gameSettings.daysPerMove, board.lastMoveTime) from " +
				ScribbleBoard.class.getName() + " board where board.gameResolution is null");
	}
}
