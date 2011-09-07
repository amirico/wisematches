package wisematches.playground.scribble.history;

import org.hibernate.HibernateException;
import org.hibernate.Query;
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
				final Query query = session.getNamedQuery("scribble.history");
				query.setFirstResult(range.getStart());
				query.setMaxResults(range.getCount());
				query.setCacheable(true);
				query.setParameter(0, personality.getId());
				return query.list();
			}
		});
	}
}
