package wisematches.playground.tourney.regular.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tourney.TourneyCareer;
import wisematches.playground.tourney.TourneyMedal;
import wisematches.playground.tourney.regular.AwardsSearchManager;
import wisematches.playground.tourney.regular.TourneyAward;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAwardsSearchManager implements AwardsSearchManager {
	private SessionFactory sessionFactory;

	public HibernateAwardsSearchManager() {
	}

	@Override
	public <Ctx extends TourneyAward.Context> int getTotalCount(Personality person, Ctx context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	public <Ctx extends TourneyAward.Context, Fl extends SearchFilter.NoFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		final Query query = createSearchQuery(person, context, true);
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Ctx extends TourneyAward.Context, Fl extends SearchFilter.NoFilter> List<TourneyAward> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
		final Query query = createSearchQuery(person, context, false);
		if (range != null) {
			range.apply(query);
		}
		return query.list();
	}


	TourneyCareer getTourneyCareer(Personality person) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("" +
				"select " +
				"sum(case when w.place=:gold then 1 else 0 end), " +
				"sum(case when w.place=:silver then 1 else 0 end), " +
				"sum(case when w.place=:bronze then 1 else 0 end) " +
				"from HibernateTourneyDivision as d join d.tourneyWinners as w " +
				"where w.player=:pid");

		query.setParameter("gold", TourneyMedal.GOLD);
		query.setParameter("silver", TourneyMedal.SILVER);
		query.setParameter("bronze", TourneyMedal.BRONZE);
		query.setLong("pid", person.getId());

		final Object[] values = (Object[]) query.uniqueResult();
		int gold = values[0] == null ? 0 : ((Number) values[0]).intValue();
		int silver = values[1] == null ? 0 : ((Number) values[1]).intValue();
		int bronze = values[2] == null ? 0 : ((Number) values[2]).intValue();

		return new RegularTourneyCareer(gold, silver, bronze);
	}

	private <Ctx extends TourneyAward.Context> Query createSearchQuery(Personality person, Ctx context, boolean count) {
		final Session session = sessionFactory.getCurrentSession();
		final boolean b = context != null && context.getMedals().size() != 0;

		final Query query = session.createQuery("" +
				"select " + (count ? "count(*)" : "new wisematches.playground.tourney.regular.impl.TourneyAwardImpl(d.tourney.number, d.finishedDate, d.language, d.section, w.place)") +
				" from HibernateTourneyDivision as d join d.tourneyWinners as w " +
				"where w.player=:pid" + (b ? " and w.place in :medals" : ""));

		query.setLong("pid", person.getId());
		if (b) {
			query.setParameterList("medals", context.getMedals());
		}
		return query;
	}

	void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
