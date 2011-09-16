package wisematches.server.web.services.search.player.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.server.web.services.search.SearchCriteria;
import wisematches.server.web.services.search.SearchParameter;
import wisematches.server.web.services.search.player.PlayerInfoBean;
import wisematches.server.web.services.search.player.PlayerSearchArea;
import wisematches.server.web.services.search.player.PlayerSearchManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerSearchManager extends HibernateDaoSupport implements PlayerSearchManager {
	private static final Constructor<?> constructor = PlayerInfoBean.class.getConstructors()[0];

	public HibernatePlayerSearchManager() {
	}

	@Override
	public int getPlayersCount(final Personality personality, final PlayerSearchArea area, final SearchCriteria criteria) {
		if (criteria != null) {
			throw new UnsupportedOperationException("Criteria is not supported at this moment");
		}
		final HibernateTemplate template = getHibernateTemplate();
		return template.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				final StringBuilder query = new StringBuilder();
				query.append("select count(distinct account.id) ");
				query.append("from wisematches.personality.account.impl.HibernateAccountImpl account, wisematches.playground.scribble.tracking.ScribbleStatisticsEditor stats ");
				appendAreaCriteria(query, area);
				query.append(" and account.id=stats.playerId");

				final Query query1 = session.createQuery(query.toString());
				query1.setCacheable(true);
				query1.setParameter("pid", personality.getId());
				return ((Number) query1.uniqueResult()).intValue();
			}
		});
	}

	@Override
	public List<PlayerInfoBean> getPlayerBeans(final Personality personality, final PlayerSearchArea area, final SearchCriteria criteria, final Range range, final Order... order) {
		if (criteria != null) {
			throw new UnsupportedOperationException("Criteria is not supported at this moment");
		}

		final HibernateTemplate template = getHibernateTemplate();
		return template.executeWithNativeSession(new HibernateCallback<List<PlayerInfoBean>>() {
			@Override
			@SuppressWarnings("unchecked")
			public List<PlayerInfoBean> doInHibernate(Session session) throws HibernateException, SQLException {
				final StringBuilder query = new StringBuilder();
				query.append("select distinct account, stats.rating, stats.activeGames, stats.finishedGames, stats.lastMoveTime, stats.averageMoveTime ");
				query.append("from wisematches.personality.account.impl.HibernateAccountImpl account, wisematches.playground.scribble.tracking.ScribbleStatisticsEditor stats ");
				appendAreaCriteria(query, area);
				query.append(" and account.id=stats.playerId");

				if (order != null && order.length != 0) {
/*
					query.append(" order by ");
					for (Order o : order) {
						final String name = o.getPropertyName();
						final Method l;
						try {
							l = PlayerInfoBean.class.getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
							SearchParameter annotation = l.getAnnotation(SearchParameter.class);
							if (annotation != null) {
								for (String s : annotation.sortingColumns()) {
									query.append(s);
									query.append(o.isAscending() ? " asc" : " desc");
								}
							}
							query.append(", ");
						} catch (NoSuchMethodException e) {
							query.append(o.toString());
						}
					}
					query.setLength(query.length() - 2);
*/
				}

				final Query q = session.createQuery(query.toString());
				q.setCacheable(true);
				q.setParameter("pid", personality.getId());
				if (range != null) {
					range.apply(q);
				}

				q.setResultTransformer(new AliasToBeanConstructorResultTransformer(constructor));
				return q.list();
			}
		}

		);
	}

	private void appendAreaCriteria(StringBuilder query, final PlayerSearchArea area) {
		switch (area) {
			case PLAYERS:
				query.append("where not account.id=:pid");
				return;
			case FRIENDS:
				query.append(", wisematches.server.web.services.friends.impl.HibernateFriendRelation r where r.friend=account.id and r.person=:pid");
				return;
			case FORMERLY:
				query.append(", wisematches.playground.tracking.RatingChange as l, wisematches.playground.tracking.RatingChange as r ");
				query.append("where r.playerId=account.id and l.boardId=r.boardId and not r.playerId=l.playerId and l.playerId=:pid");
				return;
			default:
				throw new UnsupportedOperationException("Area criteria not implemented for " + area);
		}
	}
}
