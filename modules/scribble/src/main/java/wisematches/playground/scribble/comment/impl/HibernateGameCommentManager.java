package wisematches.playground.scribble.comment.impl;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import wisematches.core.Player;
import wisematches.playground.GameBoard;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.playground.scribble.comment.GameCommentState;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateGameCommentManager implements GameCommentManager {
	private SessionFactory sessionFactory;

	public HibernateGameCommentManager() {
	}

	@Override
	public GameComment addComment(GameBoard board, Player player, String text) {
		if (board.getPlayerHand(player) == null) {
			throw new IllegalArgumentException("Specified personality '" + player + "' doesn't belong to board " + board);
		}
		HibernateGameComment comment = new HibernateGameComment(board, player, text);
		sessionFactory.getCurrentSession().save(comment);
		return comment;
	}

	@Override
	public GameComment removeComment(GameBoard board, Player player, long commentId) {
		final Session session = sessionFactory.getCurrentSession();
		HibernateGameComment comment = (HibernateGameComment) session.get(HibernateGameComment.class, commentId);
		if (comment != null) {
			if (comment.getPerson() != player.getId()) {
				return null;
			}
			if (comment.getBoard() != board.getBoardId()) {
				return null;
			}
			session.delete(comment);
			return comment;
		}
		return null;
	}

	@Override
	public int getCommentsCount(GameBoard board, Player player) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select count(*) from HibernateGameComment as c where c.board=:board");
		query.setParameter("board", board.getBoardId());
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameComment> getComments(final GameBoard board, final Player player, final long... ids) {
		final Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from HibernateGameComment as c where c.board=:board" + (ids != null && ids.length != 0 ? " and c.id in (:ids) order by c.creationDate desc, c.id desc" : ""));
		query.setParameter("board", board.getBoardId());
		if (ids != null && ids.length != 0) {
			Object[] o = new Object[ids.length];
			for (int i = 0; i < ids.length; i++) {
				o[i] = ids[i];
			}
			query.setParameterList("ids", o);
		}
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameCommentState> getCommentStates(final GameBoard board, final Player player) {
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery(
				"select c.id, (c.person=p.playerId) || (c.`read`&(1 << p.playerIndex)) != 0 from scribble_comment as c left join scribble_player as p " +
						"on c.board=p.boardId and p.playerId=:person " +
						"where c.board=:board " +
						"order by c.created desc, c.id desc");
		sqlQuery.setParameter("board", board.getBoardId());
		sqlQuery.setParameter("person", player.getId());
		sqlQuery.setResultTransformer(new ResultTransformer() {
			@Override
			public Object transformTuple(Object[] tuple, String[] aliases) {
				return new HibernateGameCommentState(((Number) tuple[0]).longValue(), tuple[1] != null && ((Number) tuple[1]).intValue() != 0);
			}

			@Override
			public List transformList(List collection) {
				return collection;
			}
		});
		return sqlQuery.list();
	}

	@Override
	public void markRead(final GameBoard board, final Player player, final long... ids) {
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery(
				"update scribble_comment as c left join scribble_player as p " +
						"on c.board=p.boardId and p.playerId=:person " +
						"set c.`read`=c.`read`|(1 << p.playerIndex) " +
						"where c.board=:board" + (ids != null && ids.length != 0 ? " and c.id in (:ids)" : ""));
		sqlQuery.setParameter("board", board.getBoardId());
		sqlQuery.setParameter("person", player.getId());
		if (ids != null && ids.length != 0) {
			final Object[] a = new Object[ids.length];
			for (int i = 0; i < ids.length; i++) {
				a[i] = ids[i];
			}
			sqlQuery.setParameterList("ids", a);
		}
		sqlQuery.executeUpdate();
	}

	@Override
	public void markUnread(final GameBoard board, final Player player, final long... ids) {
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery(
				"update scribble_comment as c left join scribble_player as p " +
						"on c.board=p.boardId and p.playerId=:person " +
						"set c.`read`=c.`read`&~(1 << p.playerIndex) " +
						"where c.board=:board" + (ids != null && ids.length != 0 ? " and c.id in (:ids)" : ""));
		sqlQuery.setParameter("board", board.getBoardId());
		sqlQuery.setParameter("person", player.getId());
		if (ids != null && ids.length != 0) {
			final Object[] a = new Object[ids.length];
			for (int i = 0; i < ids.length; i++) {
				a[i] = ids[i];
			}
			sqlQuery.setParameterList("ids", a);
		}
		sqlQuery.executeUpdate();
	}

	@Override
	public void clearComments(final GameBoard board) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("delete HibernateGameComment as c where c.board=:board");
		query.setParameter("board", board.getBoardId());
		query.executeUpdate();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
