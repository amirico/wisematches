package wisematches.server.standing.rating.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.server.player.Player;
import wisematches.server.player.member.impl.HibernatePlayerImpl;
import wisematches.server.standing.rating.PlayerRatingsManager;

import java.sql.SQLException;
import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingsManagerDao extends HibernateDaoSupport {
	public List<Player> getPlayersRating(final long fromPosition,
										 final int playersCount,
										 final PlayerRatingsManager.SortType sortType) {
		final HibernateTemplate template = getHibernateTemplate();
		final Object o = template.executeWithNativeSession(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				final Query query = session.createQuery("from " + HibernatePlayerImpl.class.getName() + " u order by u.rating " + sortType.sqlStatement());
				query.setMaxResults(playersCount);
				query.setFirstResult((int) fromPosition);
				return query.list();
			}
		});
		return (List<Player>) o;
	}

	public long getPlayersCount() {
		final HibernateTemplate template = getHibernateTemplate();
		final List list = template.find("select count(*) from " + HibernatePlayerImpl.class.getName());
		if (list.size() != 1) {
			return 0;
		}
		return (Long) list.iterator().next();
	}

	/**
	 * Returns player position starting with 1.
	 *
	 * @param playerId the player id.
	 * @return the player position starting with 1 or {@code 0} if player is unknown.
	 */
	public long getPlayerPosition(long playerId) {
		final HibernateTemplate template = getHibernateTemplate();

		final List result = template.findByNamedQuery("PlayerPosition", playerId);
		if (result.size() == 0) {
			return 0;
		}

		final Object[] values = (Object[]) result.iterator().next();
		if (values[1] == null) {
			return 0;
		}
		return (Long) values[0];
	}
}
