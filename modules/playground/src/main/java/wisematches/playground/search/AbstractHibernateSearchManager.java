package wisematches.playground.search;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.database.Order;
import wisematches.database.Range;
import wisematches.personality.Personality;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractHibernateSearchManager<T extends DesiredEntityBean<C>, C extends DesiredEntityContext> extends HibernateDaoSupport implements EntitySearchManager<T, C> {
	private final DesiredEntityDescriptor<T> entityDescriptor;

	protected AbstractHibernateSearchManager(Class<T> type) {
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
		final HibernateTemplate template = getHibernateTemplate();
		return template.executeWithNativeSession(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
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

				final Query query1 = session.createQuery(query.toString());
				query1.setCacheable(true);
				query1.setParameter("pid", person.getId());
				return ((Number) query1.uniqueResult()).intValue();
			}
		});
	}

	@Override
	public List<T> searchEntities(final Personality person, final C context, final SearchCriteria[] criteria, final Order[] order, final Range range) {
		if (order != null && order.length != 0) {
		}

		final HibernateTemplate template = getHibernateTemplate();
		return template.executeWithNativeSession(new HibernateCallback<List<T>>() {
			@Override
			@SuppressWarnings("unchecked")
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
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

				final Query query1 = session.createQuery(query.toString());
				query1.setCacheable(true);
				query1.setParameter("pid", person.getId());
				if (range != null) {
					range.apply(query1);
				}
				query1.setResultTransformer(new AliasToBeanResultTransformer(entityDescriptor.getDesiredEntityType()));
				return query1.list();
			}
		});
	}

	protected abstract String getTablesList(final C context);

	protected abstract String getWhereCriterias(final C context);
}
