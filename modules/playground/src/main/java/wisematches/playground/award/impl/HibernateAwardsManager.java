package wisematches.playground.award.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.award.*;
import wisematches.playground.search.SearchFilter;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAwardsManager implements AwardsManager {
	private SessionFactory sessionFactory;
	private final Map<String, AwardDescriptor> descriptors = new HashMap<>();

	public HibernateAwardsManager() {
	}

	@Override
	public AwardDescriptor getAwardDescriptor(String code) {
		return descriptors.get(code);
	}

	@Override
	public Collection<AwardDescriptor> getAwardDescriptors() {
		return descriptors.values();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public AwardsSummary getAwardsSummary(Personality personality) {
		final Session session = sessionFactory.getCurrentSession();
		final Query query = session.createQuery("select code, weight, count(*) from HibernateAward  where player=:pid group by code, weight");
		query.setLong("pid", personality.getId());
		return new DefaultAwardsSummary(query.list(), descriptors);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends AwardContext> int getTotalCount(Personality person, Ctx context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends AwardContext, Fl extends SearchFilter.NoFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		final Query query = createQuery(true, person, context, null);
		return ((Number) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends AwardContext, Fl extends SearchFilter.NoFilter> List<Award> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
		final Query query = createQuery(false, person, context, orders);
		if (range != null) {
			range.apply(query);
		}
		return query.list();
	}

	private Query createQuery(boolean count, Personality person, AwardContext context, Orders orders) {
		if (person == null) {
			throw new NullPointerException("Null person is not allowed for awards.");
		}

		final Session session = sessionFactory.getCurrentSession();

		final StringBuilder b = new StringBuilder();
		if (count) {
			b.append("select count(*) ");
		}
		b.append("from HibernateAward where player=:pid");

		final String code = context.getCode();
		final EnumSet<AwardWeight> weights = context.getWeights();

		if (code != null) {
			b.append(" and code=:code");
		}
		if (weights != null) {
			b.append(" and weight in (:weights)");
		}

		if (orders != null) {
			orders.apply(b);
		}
		final Query query = session.createQuery(b.toString());
		query.setLong("pid", person.getId());
		if (code != null) {
			query.setString("code", code);
		}
		if (weights != null) {
			query.setParameterList("weights", weights);
		}
		return query;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setDescriptors(Collection<AwardDescriptor> descriptors) {
		this.descriptors.clear();

		if (descriptors != null) {
			for (AwardDescriptor descriptor : descriptors) {
				final String code = descriptor.getCode();
				if (this.descriptors.put(code, descriptor) != null) {
					throw new IllegalArgumentException("Descriptor with code already registered: " + code);
				}
			}
		}
	}
}