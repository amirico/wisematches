package wisematches.server.web.services.notify.impl.manager;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.NotificationCondition;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationManager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateNotificationManager implements NotificationManager {
    private static final Integer INT_TRUE = 1;

    private SessionFactory sessionFactory;
    private final Map<String, NotificationDescriptor> descriptors = new HashMap<String, NotificationDescriptor>();

    public HibernateNotificationManager() {
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
    public NotificationCondition getNotificationCondition(Personality personality) {
        NotificationCondition condition = loadNotificationMask(personality);
        if (condition == null) {
            condition = new NotificationCondition();
            for (NotificationDescriptor d : descriptors.values()) {
                condition.setEnabled(d.getCode(), d.isEnabled());
            }
        }
        return condition;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void setNotificationCondition(Personality personality, NotificationCondition condition) {
        saveNotificationMask(personality, condition);
    }

    private NotificationCondition loadNotificationMask(final Personality personality) {
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
        final NotificationCondition m = new NotificationCondition();
        for (int i = 0; i < names.length; i++) {
            m.setEnabled(names[i], isEnabled(values[i]));
        }
        return m;
    }

    private boolean isEnabled(Object v) {
        return INT_TRUE.equals(v) || Boolean.TRUE.equals(v);
    }

    private void saveNotificationMask(final Personality personality, final NotificationCondition condition) {
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
                sqlQuery.setBoolean(names.length * j + i + 1, condition.isEnabled(names[i]));
            }
        }
        sqlQuery.executeUpdate();
    }


    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void setNotificationDescription(Collection<NotificationDescriptor> descriptors) {
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
}
