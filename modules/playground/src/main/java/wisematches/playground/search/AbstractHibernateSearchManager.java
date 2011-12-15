package wisematches.playground.search;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;

import java.util.List;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractHibernateSearchManager<T extends DesiredEntityBean<C>, C> implements EntitySearchManager<T, C> {
	private SessionFactory sessionFactory;

	private final boolean sql;
	private final DesiredEntityDescriptor<T> entityDescriptor;

	protected AbstractHibernateSearchManager(Class<T> type) {
		this(type, false);
	}

	protected AbstractHibernateSearchManager(Class<T> type, boolean sql) {
		this.sql = sql;
		entityDescriptor = new DesiredEntityDescriptor<T>(type);
	}

	@Override
	public DesiredEntityDescriptor getDescriptor() {
		return entityDescriptor;
	}

	@Override
	public int getTotalCount(Personality person, final C context) {
		return getFilteredCount(person, context, null);
	}

	@Override
	public int getFilteredCount(final Personality person, final C context, final SearchCriteria[] criteria) {
		final Session session = sessionFactory.getCurrentSession();
		final StringBuilder query = new StringBuilder();
		query.append("select ");

		query.append("count(");
		final SearchAttribute da = entityDescriptor.getDistinctAttribute();
		if (da != null) {
			query.append("distinct ");
			query.append(da.column());
		} else {
			query.append("*");
		}
		query.append(")");
		query.append(" from ");
		query.append(getTablesList(context));

		String whereCriterias = getWhereCriterias(context);
		if (whereCriterias != null) {
			query.append(" where ");
			query.append(whereCriterias);
		}

		final Query query1 = sql ? session.createSQLQuery(query.toString()) : session.createQuery(query.toString());
		query1.setCacheable(true);
		query1.setParameter("pid", person.getId());
		return ((Number) query1.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> searchEntities(final Personality person, final C context, final SearchCriteria[] criteria, final Order[] order, final Range range) {
		if (order != null && order.length != 0) {
		}

		final Session session = sessionFactory.getCurrentSession();
		final StringBuilder query = new StringBuilder();
		query.append("select ");

		final Map<String, SearchAttribute> attributes = entityDescriptor.getAttributes();

		final SearchAttribute attribute = entityDescriptor.getDistinctAttribute();
		if (attribute != null) {
			query.append("distinct ");
			query.append(attribute.column());
			query.append(" as ");
			query.append(entityDescriptor.getDistinctField());
			query.append(", ");
		}

		for (Map.Entry<String, SearchAttribute> entry : attributes.entrySet()) {
			final SearchAttribute value = entry.getValue();
			if (value == attribute) {
				continue;
			}
			query.append(value.column());
			query.append(" as ");
			query.append(entry.getKey());
			query.append(", ");
		}
		query.setLength(query.length() - 2);

		query.append(" from ");
		query.append(getTablesList(context));

		String whereCriterias = getWhereCriterias(context);
		if (whereCriterias != null) {
			query.append(" where ");
			query.append(whereCriterias);
		}

		String groupCriterias = getGroupCriterias(context);
		if (groupCriterias != null) {
			query.append(" group by ");
			query.append(groupCriterias);
		}

		if (order != null && order.length != 0) {
			query.append(" order by ");
			for (Order o : order) {
				final SearchAttribute a = attributes.get(o.getPropertyName());
				query.append(a.column());
				query.append(o.isAscending() ? " asc" : " desc");
				query.append(", ");
			}
			query.setLength(query.length() - 2);
		}

		final Query query1;
		if (sql) {
			SQLQuery q = session.createSQLQuery(query.toString());
			q.addEntity(entityDescriptor.getDesiredEntityType());
			query1 = q;
		} else {
			query1 = session.createQuery(query.toString());
			query1.setResultTransformer(new AliasToBeanResultTransformer(entityDescriptor.getDesiredEntityType()));
		}
		query1.setCacheable(true);
		query1.setParameter("pid", person.getId());
		if (range != null) {
			range.apply(query1);
		}
		return query1.list();
	}

	protected abstract String getTablesList(final C context);

	protected abstract String getWhereCriterias(final C context);

	protected abstract String getGroupCriterias(final C context);

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
