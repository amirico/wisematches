package wisematches.playground.tracking.impl;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.playground.tracking.RatingChange;
import wisematches.playground.tracking.RatingChangesCurve;
import wisematches.playground.tracking.StatisticsEditor;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernatePlayerTrackingCenterDao extends HibernateDaoSupport implements PlayerTrackingCenterDao {
	public HibernatePlayerTrackingCenterDao() {
	}

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
	public short getRating(final Personality person) {
		return getHibernateTemplate().execute(new HibernateCallback<Short>() {
			@Override
			public Short doInHibernate(Session session) throws HibernateException, SQLException {
				return ((Number) session.getNamedQuery("player.rating").setLong(0, person.getId()).uniqueResult()).shortValue();
			}
		});
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void saveRatingChange(RatingChange ratingChange) {
		getHibernateTemplate().save(ratingChange);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<RatingChange> getRatingChanges(long boardId) {
		return getHibernateTemplate().find("from wisematches.playground.tracking.RatingChange rating where rating.boardId = ?", boardId);
	}

	@Override
	public RatingChangesCurve getRatingChangesCurve(final Personality player, final int resolution, final Date startDate, final Date endDate) {
		return getHibernateTemplate().execute(new HibernateCallback<RatingChangesCurve>() {
			@Override
			public RatingChangesCurve doInHibernate(Session session) throws HibernateException, SQLException {
				final Query namedQuery = session.getNamedQuery("rating.curve");
				namedQuery.setParameter("pid", player.getId());
				namedQuery.setParameter("resolution", resolution);
				namedQuery.setParameter("start", startDate);
				namedQuery.setParameter("end", endDate);
				return new RatingChangesCurve(resolution, startDate, endDate, namedQuery.list());
			}
		});
	}
}
