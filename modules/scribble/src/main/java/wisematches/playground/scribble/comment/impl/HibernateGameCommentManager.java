package wisematches.playground.scribble.comment.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.personality.Personality;
import wisematches.playground.GameBoard;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.playground.scribble.comment.GameCommentManager;
import wisematches.playground.scribble.comment.GameCommentState;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateGameCommentManager extends HibernateDaoSupport implements GameCommentManager {
	public HibernateGameCommentManager() {
	}

	@Override
	public GameComment addComment(GameBoard board, Personality personality, String text) {
		if (board.getPlayerHand(personality.getId()) == null) {
			throw new IllegalArgumentException("Specified personality '" + personality + "' doesn't belong to board " + board);
		}
		HibernateGameComment comment = new HibernateGameComment(board, personality, text);
		getHibernateTemplate().save(comment);
		return comment;
	}

	@Override
	public GameComment removeComment(GameBoard board, Personality personality, long commentId) {
		HibernateTemplate template = getHibernateTemplate();
		HibernateGameComment comment = template.get(HibernateGameComment.class, commentId);
		if (comment != null) {
			if (comment.getPerson() != personality.getId()) {
				return null;
			}
			if (comment.getBoard() != board.getBoardId()) {
				return null;
			}
			template.delete(comment);
			return comment;
		}
		return null;
	}

	@Override
	public int getCommentsCount(GameBoard board, Personality personality) {
		final HibernateTemplate hibernateTemplate = getHibernateTemplate();
		return DataAccessUtils.intResult(hibernateTemplate.find("select count(*) from wisematches.playground.scribble.comment.impl.HibernateGameComment as c where c.board=?", board.getBoardId()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameComment> getComments(final GameBoard board, final Personality personality, final long... ids) {
		HibernateTemplate hibernateTemplate = getHibernateTemplate();
		return hibernateTemplate.executeWithNativeSession(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("from wisematches.playground.scribble.comment.impl.HibernateGameComment as c where c.board=:board" + (ids != null && ids.length != 0 ? " and c.id in (:ids) order by c.creationDate desc, c.id desc" : ""));
				query.setLong("board", board.getBoardId());
				if (ids != null && ids.length != 0) {
					Object[] o = new Object[ids.length];
					for (int i = 0; i < ids.length; i++) {
						o[i] = ids[i];
					}
					query.setParameterList("ids", o);
				}
				return query.list();
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameCommentState> getCommentStates(final GameBoard board, final Personality personality) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session session) throws HibernateException, SQLException {
				final SQLQuery sqlQuery = session.createSQLQuery(
						"select c.id, (c.person=p.playerId) || (c.`read`&(1 << p.playerIndex)) != 0 from scribble_comment as c left join scribble_player as p " +
								"on c.board=p.boardId and p.playerId=:person " +
								"where c.board=:board " +
								"order by c.created desc, c.id desc");
				sqlQuery.setLong("board", board.getBoardId());
				sqlQuery.setLong("person", personality.getId());
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
		});
	}

	@Override
	public void markRead(final GameBoard board, final Personality personality, final long... ids) {
		getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				final SQLQuery sqlQuery = session.createSQLQuery(
						"update scribble_comment as c left join scribble_player as p " +
								"on c.board=p.boardId and p.playerId=:person " +
								"set c.`read`=c.`read`|(1 << p.playerIndex) " +
								"where c.board=:board" + (ids != null && ids.length != 0 ? " and c.id in (:ids)" : ""));
				sqlQuery.setLong("board", board.getBoardId());
				sqlQuery.setLong("person", personality.getId());
				if (ids != null && ids.length != 0) {
					final Object[] a = new Object[ids.length];
					for (int i = 0; i < ids.length; i++) {
						a[i] = ids[i];
					}
					sqlQuery.setParameterList("ids", a);
				}
				return sqlQuery.executeUpdate();
			}
		});
	}

	@Override
	public void markUnread(final GameBoard board, final Personality personality, final long... ids) {
		getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				final SQLQuery sqlQuery = session.createSQLQuery(
						"update scribble_comment as c left join scribble_player as p " +
								"on c.board=p.boardId and p.playerId=:person " +
								"set c.`read`=c.`read`&~(1 << p.playerIndex) " +
								"where c.board=:board" + (ids != null && ids.length != 0 ? " and c.id in (:ids)" : ""));
				sqlQuery.setLong("board", board.getBoardId());
				sqlQuery.setLong("person", personality.getId());
				if (ids != null && ids.length != 0) {
					final Object[] a = new Object[ids.length];
					for (int i = 0; i < ids.length; i++) {
						a[i] = ids[i];
					}
					sqlQuery.setParameterList("ids", a);
				}
				return sqlQuery.executeUpdate();
			}
		});
	}

	@Override
	public void clearComments(final GameBoard board) {
		getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				final Query query = session.createQuery("delete wisematches.playground.scribble.comment.impl.HibernateGameComment as c where c.board=?");
				query.setLong(0, board.getBoardId());
				return query.executeUpdate();
			}
		});
	}
}
