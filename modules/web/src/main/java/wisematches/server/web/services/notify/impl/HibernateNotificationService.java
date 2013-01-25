package wisematches.server.web.services.notify.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import wisematches.core.Personality;
import wisematches.core.personality.player.account.Account;
import wisematches.core.task.TransactionalExecutor;
import wisematches.server.web.services.notify.*;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateNotificationService implements NotificationService, InitializingBean {
	private SessionFactory sessionFactory;
	private TransactionalExecutor taskExecutor;
	private NotificationConverter notificationConverter;

	private final Collection<NotificationPublisher> publishers = new ArrayList<>();
	private final Map<String, NotificationDescriptor> descriptorsMap = new HashMap<>();

	private static final Log log = LogFactory.getLog("wisematches.server.notice.service");

	public HibernateNotificationService() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		final Session session = sessionFactory.openSession();
		checkTable(session, "notification_settings");
//        checkTable(session, "notification_timestamp");
	}

	@Override
	public Set<String> getNotificationCodes() {
		return Collections.unmodifiableSet(descriptorsMap.keySet());
	}

	@Override
	public NotificationDescriptor getDescriptor(String code) {
		final NotificationDescriptor descriptor = descriptorsMap.get(code);
		if (descriptor == null) {
			throw new IllegalArgumentException("Unknown notification: " + code);
		}
		return descriptor;
	}

	@Override
	public NotificationScope getNotificationScope(String code, Personality personality) {
		final NotificationDescriptor descriptor = descriptorsMap.get(code);
		if (descriptor == null) {
			throw new IllegalArgumentException("Unknown notification: " + code);
		}
		final Session session = sessionFactory.getCurrentSession();
		final Query sqlQuery = session.createSQLQuery("SELECT `" + code + "` FROM notification_settings WHERE pid=:pid");
		sqlQuery.setCacheable(true);
		sqlQuery.setParameter("pid", personality.getId());

		final List list = sqlQuery.list();
		if (list.isEmpty()) {
			return descriptor.getScope();
		}

		final Number scopeOrdinal = (Number) list.get(0);
		return scopeOrdinal == null ? null : NotificationScope.values()[scopeOrdinal.intValue()];
	}

	@Override
	public NotificationScope setNotificationScope(String code, Personality personality, NotificationScope scope) {
		final String query = "INSERT INTO notification_settings (`pid`, `" + code + "`) VALUES (:pid, :scope) ON DUPLICATE KEY UPDATE `" + code + "`=:scope";
		final Session session = sessionFactory.getCurrentSession();
		final Query sqlQuery = session.createSQLQuery(query).setParameter("pid", personality.getId());
		sqlQuery.setCacheable(true);
		sqlQuery.setParameter("pid", personality.getId());
		sqlQuery.setParameter("scope", scope == null ? null : scope.ordinal());
		sqlQuery.executeUpdate();
		return null;
	}

	@Override
	public Notification raiseNotification(String code, Account target, NotificationSender sender, Object context) throws NotificationException {
		final NotificationScope personalScope;
		if (descriptorsMap.containsKey(code)) {
			personalScope = getNotificationScope(code, target);
		} else {
			personalScope = NotificationScope.EXTERNAL;
		}

		if (personalScope == null) {
			return null;
		}

		final Notification notification = notificationConverter.createNotification(code, target, sender, context);

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				for (final NotificationPublisher publisher : publishers) {
					final NotificationScope publisherScope = publisher.getNotificationScope();
					if ((publisherScope.isInternal() && personalScope.isInternal()) ||
							(publisherScope.isExternal() && personalScope.isExternal())) {
						try {
							publisher.publishNotification(notification);
						} catch (NotificationException ex) {
							log.error("Notification can't be processed: code=" + notification.getCode() + ",publisher=" + publisher.getName(), ex);
						}
					}
				}
			}
		});
		return notification;
	}

	private void checkTable(Session session, String table) {
		final SQLQuery query = session.createSQLQuery("DESCRIBE " + table);

		final List list = query.list();
		final Set<String> names = new HashSet<>();

		for (Object row : list) {
			names.add((String) ((Object[]) row)[0]);
		}
		if (!names.containsAll(descriptorsMap.keySet())) {
			final Set<String> unknown = new HashSet<>(descriptorsMap.keySet());
			unknown.removeAll(names);
			throw new IllegalStateException("Table doesn't have columns for fields: " + unknown + " for " + table);
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setNotificationConverter(NotificationConverter notificationConverter) {
		this.notificationConverter = notificationConverter;
	}

	public void setTaskExecutor(TransactionalExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setNotificationDescriptors(Collection<NotificationDescriptor> descriptors) {
		final Map<String, NotificationDescriptor> desc = new HashMap<>();
		if (descriptors != null) {
			for (NotificationDescriptor d : descriptors) {
				if (desc.put(d.getCode(), d) != null) {
					throw new IllegalArgumentException("Specified name already contains d for " + d.getCode());
				}
			}
		}
		this.descriptorsMap.clear();

		if (descriptors != null) {
			this.descriptorsMap.putAll(desc);
		}
	}

	public void setPublishers(Collection<NotificationPublisher> publishers) {
		this.publishers.clear();

		if (publishers != null) {
			this.publishers.addAll(publishers);
		}
	}
}
