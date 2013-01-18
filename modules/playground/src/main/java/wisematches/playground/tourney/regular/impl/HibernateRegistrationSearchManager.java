package wisematches.playground.tourney.regular.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import wisematches.core.Personality;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.core.search.SearchFilter;
import wisematches.playground.tourney.regular.RegistrationRecord;
import wisematches.playground.tourney.regular.RegistrationSearchManager;
import wisematches.playground.tourney.regular.Tourney;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class HibernateRegistrationSearchManager implements RegistrationSearchManager {
	private SessionFactory sessionFactory;

	public HibernateRegistrationSearchManager() {
	}

	@Override
	public <Ctx extends RegistrationRecord.Context> int getTotalCount(Personality person, Ctx context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	public <Ctx extends RegistrationRecord.Context, Fl extends SearchFilter.NoFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = HibernateQueryHelper.searchRegistrationRecords(session, context, true);
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Ctx extends RegistrationRecord.Context, Fl extends SearchFilter.NoFilter> List<RegistrationRecord> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = HibernateQueryHelper.searchRegistrationRecords(session, context, false);
		if (range != null) {
			range.apply(query);
		}
		return query.list();
	}

	@Override
	public long[] searchUnregisteredPlayers(Tourney tourney, Range range) {
		final Session session = sessionFactory.getCurrentSession();
		Query query = HibernateQueryHelper.searchUnregisteredPlayers(session, tourney);
		if (range != null) {
			range.apply(query);
		}

		int index = 0;
		final List list = query.list();
		final long[] res = new long[list.size()];
		for (Object o : list) {
			res[index++] = ((Number) o).longValue();
		}
		return res;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
