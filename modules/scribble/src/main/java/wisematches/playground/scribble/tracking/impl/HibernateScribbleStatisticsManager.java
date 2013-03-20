package wisematches.playground.scribble.tracking.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Member;
import wisematches.core.Player;
import wisematches.playground.scribble.tracking.ScribbleStatistics;
import wisematches.playground.scribble.tracking.ScribbleStatisticsManager;
import wisematches.playground.tracking.RatingCurve;
import wisematches.playground.tracking.impl.AbstractStatisticManager;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateScribbleStatisticsManager
		extends AbstractStatisticManager<ScribbleStatistics, ScribbleStatisticsEditor>
		implements ScribbleStatisticsManager {
	private SessionFactory sessionFactory;

	public HibernateScribbleStatisticsManager() {
		super(new ScribbleStatisticsTrapper());
	}

	@Override
	protected Number loadPlayerRating(Member person) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select rating from ScribbleStatisticsEditor where playerId=:pid");
		query.setParameter("pid", person.getId());
		return (Number) query.uniqueResult();
	}

	@Override
	protected ScribbleStatisticsEditor createStatistic(Member player) {
		final ScribbleStatisticsEditor editor = new ScribbleStatisticsEditor(player);
		sessionFactory.getCurrentSession().save(editor);
		return editor;
	}

	@Override
	protected void removeStatistic(Member player) {
		final Session session = sessionFactory.getCurrentSession();
		final Object o = session.get(ScribbleStatisticsEditor.class, player.getId());
		if (o != null) {
			session.delete(o);
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public RatingCurve getRatingCurve(final Player player, final int resolution, final Date startDate, final Date endDate) {
		final Session session = sessionFactory.getCurrentSession();
		final Query namedQuery = session.getNamedQuery("rating.curve");
		namedQuery.setParameter("pid", player.getId());
		namedQuery.setParameter("resolution", resolution);
		namedQuery.setDate("start", startDate);
		namedQuery.setDate("end", endDate);
		return new RatingCurve(resolution, startDate, endDate, namedQuery.list());
	}

	@Override
	protected ScribbleStatisticsEditor loadStatisticEditor(Member person) {
		return (ScribbleStatisticsEditor) sessionFactory.getCurrentSession().get(ScribbleStatisticsEditor.class, person.getId());
	}

	@Override
	protected void saveStatisticEditor(ScribbleStatisticsEditor statistic) {
		sessionFactory.getCurrentSession().update(statistic);
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
