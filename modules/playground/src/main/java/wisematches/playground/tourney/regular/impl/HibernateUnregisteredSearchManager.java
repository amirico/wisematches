package wisematches.playground.tourney.regular.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;
import wisematches.playground.tourney.regular.Tourney;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class HibernateUnregisteredSearchManager implements SearchManager<Long, Tourney.Id, SearchFilter> {
	private SessionFactory sessionFactory;

	HibernateUnregisteredSearchManager() {
	}

	@Override
	public <Ctx extends Tourney.Id> int getTotalCount(Personality person, Ctx context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	public <Ctx extends Tourney.Id, Fl extends SearchFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createSQLQuery("SELECT count(a.id) " +
				"FROM account_personality as a left join tourney_regular_subs as s " +
				"ON a.id=s.player " +
				"WHERE s.tourneyNumber is null or s.tourneyNumber!=:tourney");
		query.setInteger("tourney", context.getNumber());
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Ctx extends Tourney.Id, Fl extends SearchFilter> List<Long> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
		final Session session = sessionFactory.getCurrentSession();

		final Query query = session.createSQLQuery("SELECT a.id " +
				"FROM account_personality as a left join tourney_regular_subs as s " +
				"ON a.id=s.player " +
				"WHERE s.tourneyNumber is null or s.tourneyNumber!=:tourney " +
				"ORDER by a.id");
		query.setInteger("tourney", context.getNumber());

		if (range != null) {
			range.apply(query);
		}
		return query.list();
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
