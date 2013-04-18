package wisematches.playground.scribble.comment.impl;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.ResultTransformer;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
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
	public <Ctx extends GameBoard> int getTotalCount(Personality person, Ctx context) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select count(*) from HibernateGameComment as c where c.board=:board");
		query.setParameter("board", context.getBoardId());
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Ctx extends GameBoard> List<GameCommentState> searchEntities(Personality person, Ctx context, Orders orders, Range range) {
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery(
				"SELECT c.id, (c.person=p.playerId) || (c.`read`&(1 << p.playerIndex)) != 0 FROM scribble_comment AS c LEFT JOIN scribble_player AS p " +
						"ON c.board=p.boardId AND p.playerId=:person " +
						"WHERE c.board=:board " +
						(orders != null ? orders.toSqlOrders() : "ORDER BY c.created DESC, c.id DESC"));
		sqlQuery.setParameter("board", context.getBoardId());
		sqlQuery.setParameter("person", person.getId());
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
		if (range != null) {
			range.apply(sqlQuery);
		}
		return sqlQuery.list();
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
	public void markRead(final GameBoard board, final Player player, final long... ids) {
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery(
				"UPDATE scribble_comment AS c LEFT JOIN scribble_player AS p " +
						"ON c.board=p.boardId AND p.playerId=:person " +
						"SET c.`read`=c.`read`|(1 << p.playerIndex) " +
						"WHERE c.board=:board" + (ids != null && ids.length != 0 ? " AND c.id IN (:ids)" : ""));
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
				"UPDATE scribble_comment AS c LEFT JOIN scribble_player AS p " +
						"ON c.board=p.boardId AND p.playerId=:person " +
						"SET c.`read`=c.`read`&~(1 << p.playerIndex) " +
						"WHERE c.board=:board" + (ids != null && ids.length != 0 ? " AND c.id IN (:ids)" : ""));
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
