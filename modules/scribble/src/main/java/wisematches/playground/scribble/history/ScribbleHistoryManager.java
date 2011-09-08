package wisematches.playground.scribble.history;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.history.GameHistory;
import wisematches.playground.history.GameHistoryManager;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleHistoryManager extends HibernateDaoSupport implements GameHistoryManager {
	private static final String QUERY = "select " +
			"b.*, " +
			"max(if(r.playerIndex=0, r.playerId, 0)) player0, " +
			"max(if(r.playerIndex=1, r.playerId, 0)) player1, " +
			"max(if(r.playerIndex=2, r.playerId, 0)) player2, " +
			"max(if(r.playerIndex=3, r.playerId, 0)) player3 " +
			"from " +
			"	(select " +
			"		board.boardId, board.rated, UPPER(board.language) language, " +
			"		board.resolution, board.movesCount, board.startedDate, board.finishedDate, " +
			"		rating.newRating, rating.oldRating " +
			"	from " +
			"		scribble_board as board, scribble_player as hand, rating_history as rating " +
			"	where " +
			"		not board.resolution is null and " +
			"		board.boardId=hand.boardId and " +
			"		board.boardId=rating.boardId and " +
			"		rating.playerId=hand.playerId and " +
			"		hand.playerId=? ${order} limit ?, ?) as b, " +
			"	scribble_player as r " +
			"where " +
			"	r.boardId=b.boardId group by b.boardId ${order}";

	public ScribbleHistoryManager() {
	}

	@Override
	public int getFinishedGamesCount(Personality personality) {
		final HibernateTemplate template = getHibernateTemplate();
		return DataAccessUtils.intResult(template.find("select count(*) from wisematches.playground.scribble.ScribbleBoard board join board.playerHands hand " +
				"where board.gameResolution is not NULL and hand.playerId = ?", personality.getId()));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GameHistory> getFinishedGames(final Personality personality, final Range range, final Order... orders) {
		final HibernateTemplate template = getHibernateTemplate();
		return template.executeWithNativeSession(new HibernateCallback<List>() {
			@Override
			public List doInHibernate(Session session) throws HibernateException, SQLException {
				final String sqlQuery;
				if (orders != null && orders.length != 0) {
					final StringBuilder b = new StringBuilder();
					for (Order order : orders) {
						if (b.length() != 0) {
							b.append(", ");
						}
						b.append(order.toString());
					}
					b.insert(0, "order by ");
					sqlQuery = QUERY.replaceAll("\\$\\{order\\}", b.toString());
				} else {
					sqlQuery = QUERY.replaceAll("\\$\\{order\\}", "");
				}

				final SQLQuery query = session.createSQLQuery(sqlQuery);
				query.addRoot("history", ScribbleGameHistory.class);
				query.setCacheable(true);
				query.setParameter(0, personality.getId());
				query.setParameter(1, range.getFirstResult());
				query.setParameter(2, range.getMaxResults());

				return query.list();
			}
		});
	}
}
