package wisematches.playground.tracking.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.tracking.RatingCurve;
import wisematches.playground.tracking.StatisticsEditor;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerTrackingCenterDao implements PlayerTrackingCenterDao {
	private SessionFactory sessionFactory;

	public HibernatePlayerTrackingCenterDao() {
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T extends StatisticsEditor> T loadPlayerStatistic(Class<? extends T> clazz, Personality personality) {
		return (T) sessionFactory.getCurrentSession().get(clazz, personality.getId());
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void savePlayerStatistic(StatisticsEditor statistic) {
		sessionFactory.getCurrentSession().saveOrUpdate(statistic);
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void removePlayerStatistic(StatisticsEditor statistic) {
		sessionFactory.getCurrentSession().delete(statistic);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public short getRating(final Personality person) {
		final Session session = sessionFactory.getCurrentSession();
		return ((Number) session.getNamedQuery("player.rating").setLong(0, person.getId()).uniqueResult()).shortValue();
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public RatingCurve getRatingChangesCurve(final Personality player, final int resolution, final Date startDate, final Date endDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Query namedQuery = session.getNamedQuery("rating.curve");
		namedQuery.setParameter("pid", player.getId());
		namedQuery.setParameter("resolution", resolution);
		namedQuery.setParameter("start", startDate);
		namedQuery.setParameter("end", endDate);
		return new RatingCurve(resolution, startDate, endDate, namedQuery.list());
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
