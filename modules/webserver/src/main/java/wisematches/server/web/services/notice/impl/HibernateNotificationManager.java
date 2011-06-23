package wisematches.server.web.services.notice.impl;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notice.NotificationDescription;
import wisematches.server.web.services.notice.NotificationManager;
import wisematches.server.web.services.notice.NotificationMask;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateNotificationManager extends HibernateDaoSupport implements NotificationManager {
    private final Map<String, NotificationDescription> descriptionMap = new HashMap<String, NotificationDescription>();

    public HibernateNotificationManager() {
    }

    @Override
    public Collection<NotificationDescription> getDescriptions() {
        return Collections.unmodifiableCollection(descriptionMap.values());
    }

    @Override
    public boolean isNotificationEnabled(final String name, final Personality personality) {
        final Object lastUpdated = getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                final SQLQuery sqlQuery = session.createSQLQuery("select `" + name + "` from settings_notice where pid=?");
                sqlQuery.setLong(0, personality.getId());
                return sqlQuery.uniqueResult();
            }
        });
        return lastUpdated != null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NotificationMask getNotificationMask(Personality personality) {
        NotificationMask notificationMask = loadNotificationMask(personality);
        if (notificationMask == null) {
            notificationMask = new NotificationMask();
            for (NotificationDescription d : descriptionMap.values()) {
                notificationMask.setEnabled(d.getName(), d.isEnabled());
            }
        }
        return notificationMask;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void setNotificationMask(Personality personality, NotificationMask mask) {
        saveNotificationMask(personality, mask);
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

    private NotificationMask loadNotificationMask(final Personality personality) {
        return getHibernateTemplate().execute(new HibernateCallback<NotificationMask>() {
            @Override
            public NotificationMask doInHibernate(Session session) throws HibernateException, SQLException {
                final String[] names = descriptionMap.keySet().toArray(new String[descriptionMap.keySet().size()]);
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
                final NotificationMask m = new NotificationMask();
                for (int i = 0; i < names.length; i++) {
                    m.setEnabled(names[i], values[i] != null);
                }
                return m;
            }
        });
    }

    private void saveNotificationMask(final Personality personality, final NotificationMask mask) {
        getHibernateTemplate().execute(new HibernateCallback<Void>() {
            @Override
            public Void doInHibernate(Session session) throws HibernateException, SQLException {
                final String[] names = descriptionMap.keySet().toArray(new String[descriptionMap.keySet().size()]);
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
                        if (mask.isEnabled(names[i])) {
                            sqlQuery.setTimestamp(names.length * j + i + 1, new Timestamp(System.currentTimeMillis()));
                        } else {
                            sqlQuery.setTimestamp(names.length * j + i + 1, null);
                        }
                    }
                }
                sqlQuery.executeUpdate();
                return null;
            }
        });
    }
}
