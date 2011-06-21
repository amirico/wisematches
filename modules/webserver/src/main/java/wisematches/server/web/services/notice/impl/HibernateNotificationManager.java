package wisematches.server.web.services.notice.impl;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import wisematches.personality.Personality;
import wisematches.server.web.services.notice.NotificationDescription;
import wisematches.server.web.services.notice.NotificationManager;
import wisematches.server.web.services.notice.NotificationMask;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateNotificationManager extends HibernateDaoSupport implements NotificationManager {
	private final Map<Personality, NotificationMask> maskMap = new HashMap<Personality, NotificationMask>();
	private final Map<String, NotificationDescription> descriptionMap = new HashMap<String, NotificationDescription>();

	public HibernateNotificationManager() {
	}

	@Override
	public boolean isNotificationEnabled(final String name, final Personality personality) {
		getHibernateTemplate().execute(new HibernateCallback<Boolean>() {
			@Override
			public Boolean doInHibernate(Session session) throws HibernateException, SQLException {
				final SQLQuery sqlQuery = session.createSQLQuery("select " + name + " from where pid=?");
				sqlQuery.setLong(1, personality.getId());
				return null;//sqlQuery.uniqueResult();
			}
		});
		throw new UnsupportedOperationException("TODO: not implemented");
	}

	@Override
	public boolean[] isNotificationEnabled(String name, Personality[] personalities) {
		throw new UnsupportedOperationException("TODO: not implemented");
	}

	@Override
	public NotificationMask getNotificationMask(Personality personality) {
		NotificationMask notificationMask = maskMap.get(personality);
		if (notificationMask == null) {
			notificationMask = new NotificationMask();
			for (NotificationDescription d : descriptionMap.values()) {
				notificationMask.setEnabled(d.getName(), d.isEnabled());
			}
			maskMap.put(personality, notificationMask);
		}
		return notificationMask;
	}

	@Override
	public void setNotificationMask(Personality personality, NotificationMask mask) {
		maskMap.put(personality, mask);
	}

	public void setNotificationDescription(Collection<NotificationDescription> descriptions) {
		final Map<String, NotificationDescription> desc = new HashMap<String, NotificationDescription>();
		if (descriptions != null) {
			for (NotificationDescription d : descriptions) {
				if (desc.put(d.getName(), d) != null) {
					throw new IllegalArgumentException("Specified name already contains d for " + d.getName());
				}
			}
		}
		descriptionMap.clear();
		descriptionMap.putAll(desc);
	}
}
