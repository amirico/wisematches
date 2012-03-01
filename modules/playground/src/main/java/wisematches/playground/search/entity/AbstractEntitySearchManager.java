package wisematches.playground.search.entity;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchCriteria;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractEntitySearchManager<E, C> implements EntitySearchManager<E, C> {
	private final Class<?> entityType;

	private SessionFactory sessionFactory;

	protected AbstractEntitySearchManager(Class<?> entityType) {
		this.entityType = entityType;
	}

	@Override
	public int getTotalCount(Personality person, C context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	public int getFilteredCount(Personality person, C context, SearchCriteria[] criterias) {
		final Session session = sessionFactory.getCurrentSession();

		final Criteria criteria = session.createCriteria(entityType);
		applyRestrictions(criteria, context, criterias);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<E> searchEntities(Personality person, C context, SearchCriteria[] criterias, Order[] orders, Range range) {
		final Session session = sessionFactory.getCurrentSession();

		final Criteria criteria = session.createCriteria(entityType);
		applyRestrictions(criteria, context, criterias);
		applyProjections(criteria, context, criterias);
		applyOrders(criteria, orders);
		applyRange(range, criteria);
		return criteria.list();
	}

	protected abstract void applyRestrictions(Criteria criteria, C context, SearchCriteria[] criterias);

	protected abstract void applyProjections(Criteria criteria, C context, SearchCriteria[] criterias);

	protected void applyRange(Range range, Criteria criteria) {
		if (range != null) {
			criteria.setFirstResult(range.getFirstResult());
			criteria.setMaxResults(range.getMaxResults());
		}
	}

	protected void applyOrders(Criteria criteria, Order[] orders) {
		if (orders != null) {
			for (Order order1 : orders) {
				if (order1.isAscending()) {
					criteria.addOrder(org.hibernate.criterion.Order.asc(order1.getPropertyName()));
				} else {
					criteria.addOrder(org.hibernate.criterion.Order.desc(order1.getPropertyName()));
				}
			}
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}