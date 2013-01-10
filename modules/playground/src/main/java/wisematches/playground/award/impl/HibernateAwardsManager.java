package wisematches.playground.award.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.award.Award;
import wisematches.playground.award.AwardDescriptor;
import wisematches.playground.award.AwardsManager;
import wisematches.playground.award.AwardsSummary;
import wisematches.playground.search.SearchFilter;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		return new DefaultAwardsSummary(query.list());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends Void> int getTotalCount(Personality person, Ctx context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends Void, Fl extends SearchFilter.NoFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public <Ctx extends Void, Fl extends SearchFilter.NoFilter> List<Award> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
		throw new UnsupportedOperationException("TODO: Not implemented");
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