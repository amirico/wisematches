package wisematches.server.web.services.notify.impl.manager;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MySQLNotificationManager implements NotificationManager {
    private SessionFactory sessionFactory;
    private NotificationDistributor notificationDescriptor;

    private final List<NotificationDescriptor> descriptors = new ArrayList<NotificationDescriptor>();
    private final Map<String, NotificationDescriptor> descriptorsMap = new HashMap<String, NotificationDescriptor>();
    private final TheNotificationDistributorListener distributorListener = new TheNotificationDistributorListener();
    private final Collection<NotificationManagerListener> listeners = new CopyOnWriteArraySet<NotificationManagerListener>();

    // Disabled is value is 0
    private static final Timestamp DISABLED = new Timestamp(0);

    public MySQLNotificationManager() {
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
        return descriptorsMap.get(code);
    }

    @Override
    public Collection<NotificationDescriptor> getDescriptors() {
        return Collections.unmodifiableCollection(descriptors);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Date getNotificationDate(Personality personality, String code) {
        final NotificationDescriptor descriptor = descriptorsMap.get(code);
        if (descriptor == null) {
            throw new IllegalArgumentException("Unknown notification: " + code);
        }
        final Session session = sessionFactory.getCurrentSession();
        final Query sqlQuery = session.createSQLQuery("select `" + code + "` from player_notification where pid=?").setLong(0, personality.getId());
        final Object v = sqlQuery.uniqueResult();
        if (!isNotificationEnabled(v)) {
            return null;
        }
        return (Date) v;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean isNotificationEnabled(Personality personality, String code) {
        final NotificationDescriptor descriptor = descriptorsMap.get(code);
        if (descriptor == null) {
            throw new IllegalArgumentException("Unknown notification: " + code);
        }
        final Session session = sessionFactory.getCurrentSession();
        final Query sqlQuery = session.createSQLQuery("select `" + code + "` from player_notification where pid=?").setLong(0, personality.getId());
        return isNotificationEnabled(sqlQuery.uniqueResult());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NotificationSettings getNotificationSettings(Personality personality) {
        final Session session = sessionFactory.getCurrentSession();

        final StringBuilder b = new StringBuilder();
        for (NotificationDescriptor descriptor : descriptors) {
            b.append("`");
            b.append(descriptor.getCode());
            b.append("`");
            b.append(",");
        }
        b.setLength(b.length() - 1);
        final Query sqlQuery = session.createSQLQuery("select " + b + " from player_notification where pid=?").setLong(0, personality.getId());
        final Object[] values = (Object[]) sqlQuery.uniqueResult();

        int index = 0;
        final NotificationSettings settings = new NotificationSettings();
        for (Iterator<NotificationDescriptor> iterator = descriptors.iterator(); iterator.hasNext(); index++) {
            NotificationDescriptor descriptor = iterator.next();
            if (values == null) {
                settings.setEnabled(descriptor.getCode(), descriptor.isEnabled());
            } else {
                settings.setEnabled(descriptor.getCode(), isNotificationEnabled(values[index]));
            }
        }
        return settings;
    }

    @Override
    public void setNotificationSettings(Personality personality, NotificationSettings settings) {
        final List<String> changes = new ArrayList<String>();
        final NotificationSettings oldSettings = getNotificationSettings(personality);
        for (String code : descriptorsMap.keySet()) {
            if (oldSettings.isEnabled(code) != settings.isEnabled(code)) { // state was changed
                changes.add(code);
            }
        }

        if (changes.isEmpty()) { // nothing was changed
            return;
        }

        final Session session = sessionFactory.getCurrentSession();
        final StringBuilder b = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final StringBuilder b3 = new StringBuilder();
        for (String name : changes) {
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

        final String query = "INSERT INTO player_notification (`pid`, " + b + ") VALUES (?, " + b2 + ") ON DUPLICATE KEY UPDATE " + b3;
        final Query sqlQuery = session.createSQLQuery(query).setLong(0, personality.getId());
        for (int j = 0; j < 2; j++) {
            for (int i = 0, changesSize = changes.size(); i < changesSize; i++) {
                final String change = changes.get(i);
                if (settings.isEnabled(change)) {
                    sqlQuery.setTimestamp(changesSize * j + i + 1, null);
                } else {
                    sqlQuery.setTimestamp(changesSize * j + i + 1, DISABLED);
                }
            }
        }
        sqlQuery.executeUpdate();

        for (NotificationManagerListener listener : listeners) {
            listener.notificationConditionChanged(personality, oldSettings, settings);
        }
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
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
        this.descriptorsMap.clear();

        this.descriptors.addAll(descriptors);
        this.descriptorsMap.putAll(desc);

        checkNotificationTable();
    }

    public void setNotificationDistributor(NotificationDistributor notificationDescriptor) {
        if (this.notificationDescriptor != null) {
            this.notificationDescriptor.removeNotificationDistributorListener(distributorListener);
        }

        this.notificationDescriptor = notificationDescriptor;
        checkNotificationTable();

        if (this.notificationDescriptor != null) {
            this.notificationDescriptor.addNotificationDistributorListener(distributorListener);
        }
    }

    private void checkNotificationTable() {
        if (sessionFactory == null || descriptors.isEmpty()) {
            return;
        }

        final Session session = sessionFactory.openSession();
        final SQLQuery query = session.createSQLQuery("DESCRIBE player_notification");

        final List list = query.list();
        final Set<String> names = new HashSet<String>();

        for (Object row : list) {
            names.add((String) ((Object[]) row)[0]);
        }
        if (!names.containsAll(descriptorsMap.keySet())) {
            final Set<String> unknown = new HashSet<String>(descriptorsMap.keySet());
            unknown.removeAll(names);
            throw new IllegalStateException("Table doesn't have columns for fields: " + unknown);
        }
    }

    private void updateNotificationDate(Notification notification, Date date) {
        if (!isNotificationEnabled(notification.getRecipient(), notification.getCode())) {
            return;
        }

        final Timestamp timestamp = new Timestamp(date.getTime());
        final Session session = sessionFactory.getCurrentSession();
        final String query = "INSERT INTO player_notification (`pid`, `" + notification.getCode() + "`) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE `" + notification.getCode() + "`=?";
        final Query sqlQuery = session.createSQLQuery(query).setLong(0, notification.getRecipient().getId());
        sqlQuery.setTimestamp(1, timestamp).setTimestamp(2, timestamp).executeUpdate();
    }

    private boolean isNotificationEnabled(Object v) {
        return !DISABLED.equals(v);
    }

    private class TheNotificationDistributorListener implements NotificationDistributorListener {
        private TheNotificationDistributorListener() {
        }

        @Override
        public void notificationRejected(Notification notification, PublicationType type) {
        }

        @Override
        public void notificationPublished(Notification notification, PublicationType type) {
            if (type == PublicationType.EXTERNAL) {
                updateNotificationDate(notification, new Date());
            }
        }
    }
}
