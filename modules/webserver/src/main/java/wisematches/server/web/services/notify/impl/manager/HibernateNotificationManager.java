package wisematches.server.web.services.notify.impl.manager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.*;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateNotificationManager implements NotificationManager {
	private static final Integer INT_TRUE = 1;

	private SessionFactory sessionFactory;
	private NotificationDistributor notificationDescriptor;

	private final Map<String, NotificationDescriptor> descriptors = new HashMap<String, NotificationDescriptor>();
	private final Collection<NotificationManagerListener> listeners = new ArrayList<NotificationManagerListener>();
	private final TheNotificationDistributorListener distributorListener = new TheNotificationDistributorListener();

	public HibernateNotificationManager() {
	}

	@Override
	public void addNotificationManagerListener(NotificationManagerListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeNotificationManagerListener(NotificationManagerListener l) {
		listeners.remove(l);
	}

	@Override
	public NotificationDescriptor getDescriptor(String code) {
		return descriptors.get(code);
	}

	@Override
	public Collection<NotificationDescriptor> getDescriptors() {
		return Collections.unmodifiableCollection(descriptors.values());
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Date getNotificationDate(Personality personality, String code) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public boolean isNotificationEnabled(Personality personality, String code) {
		final NotificationDescriptor descriptor = descriptors.get(code);
		if (descriptor == null) {
			throw new NullPointerException("Description can't be null");
		}
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery("select `" + code + "` from settings_notice where pid=?");
		sqlQuery.setLong(0, personality.getId());
		final Object v = sqlQuery.uniqueResult();
		if (v == null) {
			return descriptor.isEnabled();
		}
		return isEnabled(v);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public NotificationSettings getNotificationCondition(Personality personality) {
		NotificationSettings settings = loadNotificationMask(personality);
		if (settings == null) {
			settings = new NotificationSettings();
			for (NotificationDescriptor d : descriptors.values()) {
				settings.setEnabled(d.getCode(), d.isEnabled());
			}
		}
		return settings;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void setNotificationCondition(Personality personality, NotificationSettings settings) {
		saveNotificationMask(personality, settings);
	}


	private boolean isEnabled(Object v) {
		return INT_TRUE.equals(v) || Boolean.TRUE.equals(v);
	}

	private NotificationSettings loadNotificationMask(final Personality personality) {
		final Session session = sessionFactory.getCurrentSession();
		final String[] names = descriptors.keySet().toArray(new String[descriptors.keySet().size()]);
		final StringBuilder b = new StringBuilder();
		for (String name : names) {
			b.append("`");
			b.append(name);
			b.append("`");
			b.append(",");
		}
		b.setLength(b.length() - 1);
		final SQLQuery sqlQuery = session.createSQLQuery("select " + b + " from settings_notice where pid=?");
		sqlQuery.setLong(0, personality.getId());

		final Object[] values = (Object[]) sqlQuery.uniqueResult();
		if (values == null) {
			return null;
		}
		final NotificationSettings m = new NotificationSettings();
		for (int i = 0; i < names.length; i++) {
			m.setEnabled(names[i], isEnabled(values[i]));
		}
		return m;
	}

	private void saveNotificationMask(final Personality personality, final NotificationSettings settings) {
		final Session session = sessionFactory.getCurrentSession();
		final String[] names = descriptors.keySet().toArray(new String[descriptors.keySet().size()]);
		final StringBuilder b = new StringBuilder();
		final StringBuilder b2 = new StringBuilder();
		final StringBuilder b3 = new StringBuilder();
		for (String name : names) {
			b.append("`");
			b.append(name);
			b.append("`");
			b.append(",");

			b2.append("?,");

			b3.append("`");
			b3.append(name);
			b3.append("`");
			b3.append("=?");
			b3.append(",");
		}
		b.setLength(b.length() - 1);
		b2.setLength(b2.length() - 1);
		b3.setLength(b3.length() - 1);
		final SQLQuery sqlQuery = session.createSQLQuery("INSERT INTO settings_notice (`pid`, " + b + ") " +
				"VALUES (?, " + b2 + ")" +
				" ON DUPLICATE KEY UPDATE " + b3);
		sqlQuery.setLong(0, personality.getId());
		for (int j = 0; j < 2; j++) {
			for (int i = 0; i < names.length; i++) {
				sqlQuery.setBoolean(names.length * j + i + 1, settings.isEnabled(names[i]));
			}
		}
		sqlQuery.executeUpdate();
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setNotificationDescriptor(NotificationDistributor notificationDescriptor) {
		if (this.notificationDescriptor != null) {
			this.notificationDescriptor.removeNotificationDistributorListener(distributorListener);
		}

		this.notificationDescriptor = notificationDescriptor;

		if (this.notificationDescriptor != null) {
			this.notificationDescriptor.addNotificationDistributorListener(distributorListener);
		}
	}

	public void setNotificationDescriptors(Collection<NotificationDescriptor> descriptors) {
		final Map<String, NotificationDescriptor> desc = new HashMap<String, NotificationDescriptor>();
		if (descriptors != null) {
			for (NotificationDescriptor d : descriptors) {
				if (desc.put(d.getCode(), d) != null) {
					throw new IllegalArgumentException("Specified name already contains d for " + d.getCode());
				}
			}
		}
		this.descriptors.clear();
		this.descriptors.putAll(desc);
	}

	private class TheNotificationDistributorListener implements NotificationDistributorListener {
		private TheNotificationDistributorListener() {
		}

		@Override
		public void notificationRejected(Notification notification, PublicationType type) {
		}

		@Override
		public void notificationPublished(Notification notification, PublicationType type) {
			throw new UnsupportedOperationException("TODO: Not implemented");
		}
	}
}
