package wisematches.playground.search.entity;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractEntitySearchManager<E, C, F extends SearchFilter> implements EntitySearchManager<E, C, F> {
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
	public int getFilteredCount(Personality person, C context, F filter) {
		final Session session = sessionFactory.getCurrentSession();

		final Criteria criteria = session.createCriteria(entityType);
		applyRestrictions(criteria, context, filter);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<E> searchEntities(Personality person, C context, F filter, Orders orders, Range range) {
		final Session session = sessionFactory.getCurrentSession();

		final Criteria criteria = session.createCriteria(entityType);
		applyRestrictions(criteria, context, filter);
		applyProjections(criteria, context, filter);
		applyOrders(criteria, orders);
		applyRange(range, criteria);
		return criteria.list();
	}

	protected abstract void applyRestrictions(Criteria criteria, C context, SearchFilter filters);

	protected abstract void applyProjections(Criteria criteria, C context, SearchFilter filters);

	protected void applyRange(Range range, Criteria criteria) {
		if (range != null) {
			range.apply(criteria);
		}
	}

	protected void applyOrders(Criteria criteria, Orders orders) {
		if (orders != null) {
			orders.apply(criteria);
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}