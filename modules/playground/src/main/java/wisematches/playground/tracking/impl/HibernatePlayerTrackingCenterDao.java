package wisematches.playground.tracking.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.tracking.RatingCurve;
import wisematches.playground.tracking.StatisticsEditor;

import java.sql.SQLException;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerTrackingCenterDao extends HibernateDaoSupport implements PlayerTrackingCenterDao {
	public HibernatePlayerTrackingCenterDao() {
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T extends StatisticsEditor> T loadPlayerStatistic(Class<? extends T> clazz, Personality personality) {
		return getHibernateTemplate().get(clazz, personality.getId());
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void savePlayerStatistic(StatisticsEditor statistic) {
		getHibernateTemplate().saveOrUpdate(statistic);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void removePlayerStatistic(StatisticsEditor statistic) {
		getHibernateTemplate().delete(statistic);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public short getRating(final Personality person) {
		return getHibernateTemplate().execute(new HibernateCallback<Short>() {
			@Override
			public Short doInHibernate(Session session) throws HibernateException, SQLException {
				return ((Number) session.getNamedQuery("player.rating").setLong(0, person.getId()).uniqueResult()).shortValue();
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public RatingCurve getRatingChangesCurve(final Personality player, final int resolution, final Date startDate, final Date endDate) {
		return getHibernateTemplate().execute(new HibernateCallback<RatingCurve>() {
			@Override
			public RatingCurve doInHibernate(Session session) throws HibernateException, SQLException {
				final Query namedQuery = session.getNamedQuery("rating.curve");
				namedQuery.setParameter("pid", player.getId());
				namedQuery.setParameter("resolution", resolution);
				namedQuery.setParameter("start", startDate);
				namedQuery.setParameter("end", endDate);
				return new RatingCurve(resolution, startDate, endDate, namedQuery.list());
			}
		});
	}
}
