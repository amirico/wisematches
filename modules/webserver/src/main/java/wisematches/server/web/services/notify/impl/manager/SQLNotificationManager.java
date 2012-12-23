package wisematches.server.web.services.notify.impl.manager;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationScope;
import wisematches.server.web.services.notify.NotificationSettings;
import wisematches.server.web.services.notify.impl.NotificationManager;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SQLNotificationManager implements NotificationManager, InitializingBean {
    private SessionFactory sessionFactory;

    private final List<NotificationDescriptor> descriptors = new ArrayList<>();
    private final Map<String, NotificationDescriptor> descriptorsMap = new HashMap<>();

    public SQLNotificationManager() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        checkNotificationTables();
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
    public Date getNotificationDate(Personality personality, String code) {
        final Session session = sessionFactory.getCurrentSession();
        final Query sqlQuery = session.createSQLQuery("select `" + code + "` from notification_timestamp where pid=:pid");
        sqlQuery.setLong("pid", personality.getId());
        return (Date) sqlQuery.uniqueResult();
    }

    @Override
    public void updateNotificationDate(Personality personality, String code) {
        final Session session = sessionFactory.getCurrentSession();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        final String query = "INSERT INTO notification_timestamp (`pid`, `" + code + "`) VALUES (:pid, :time) " +
                "ON DUPLICATE KEY UPDATE `" + code + "`=:time";
        final Query sqlQuery = session.createSQLQuery(query);
        sqlQuery.setLong("pid", personality.getId());
        sqlQuery.setTimestamp("time", timestamp);
        sqlQuery.executeUpdate();
    }

    @Override
    public NotificationScope getNotificationScope(Personality personality, String code) {
        final NotificationDescriptor descriptor = descriptorsMap.get(code);
        if (descriptor == null) {
            throw new IllegalArgumentException("Unknown notification: " + code);
        }
        final Session session = sessionFactory.getCurrentSession();
        final Query sqlQuery = session.createSQLQuery("select `:code` from notification_settings where pid=:pid");
        sqlQuery.setCacheable(true);
        sqlQuery.setLong("pid", personality.getId());
        sqlQuery.setString("code", code);
        return (NotificationScope) sqlQuery.uniqueResult();
    }

    @Override
    public NotificationSettings getNotificationSettings(Personality personality) {
        final StringBuilder b = new StringBuilder();
        for (NotificationDescriptor descriptor : descriptors) {
            b.append("`");
            b.append(descriptor.getCode());
            b.append("`");
            b.append(",");
        }
        b.setLength(b.length() - 1);

        final Session session = sessionFactory.getCurrentSession();
        final Query sqlQuery = session.createSQLQuery("select " + b + " from notification_settings where pid=:pid");
        sqlQuery.setLong("pid", personality.getId());
        final Object[] values = (Object[]) sqlQuery.uniqueResult();

        final NotificationSettings settings = new NotificationSettings(descriptors);
        if (values != null) {
            int index = 0;
            for (Iterator<NotificationDescriptor> iterator = descriptors.iterator(); iterator.hasNext(); index++) {
                NotificationDescriptor descriptor = iterator.next();
                settings.setNotificationScope(descriptor.getCode(), (NotificationScope) values[index]);
            }
        }
        return settings;
    }

    @Override
    public NotificationSettings updateNotificationSettings(Personality personality, NotificationSettings settings) {
        final List<String> changes = new ArrayList<>();
        final NotificationSettings oldSettings = getNotificationSettings(personality);
        for (String code : descriptorsMap.keySet()) {
            if (oldSettings.getNotificationScope(code) != settings.getNotificationScope(code)) { // state was changed
                changes.add(code);
            }
        }

        if (changes.isEmpty()) { // nothing was changed
            return null;
        }

        final Session session = sessionFactory.getCurrentSession();
        final StringBuilder b = new StringBuilder();
        final StringBuilder b2 = new StringBuilder();
        final StringBuilder b3 = new StringBuilder();

        for (int i1 = 0; i1 < changes.size(); i1++) {
            String name = changes.get(i1);
            b.append("`");
            b.append(name);
            b.append("`");
            b.append(",");

            b2.append(":change").append(i1).append(",");

            b3.append("`");
            b3.append(name);
            b3.append("`");
            b3.append("=");
            b3.append(":change").append(i1).append(",");
        }
        b.setLength(b.length() - 1);
        b2.setLength(b2.length() - 1);
        b3.setLength(b3.length() - 1);

        final String query = "INSERT INTO notification_settings (`pid`, " + b + ") VALUES (:pid, " + b2 + ") ON DUPLICATE KEY UPDATE " + b3;
        final Query sqlQuery = session.createSQLQuery(query).setLong("pid", personality.getId());
        for (int i = 0, changesSize = changes.size(); i < changesSize; i++) {
            final String change = changes.get(i);
            sqlQuery.setParameter("change" + i, settings.getNotificationScope(change));
        }
        sqlQuery.executeUpdate();
        return oldSettings;
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
        this.descriptors.clear();
        this.descriptorsMap.clear();

        if (descriptors != null) {
            this.descriptors.addAll(descriptors);
            this.descriptorsMap.putAll(desc);
        }
    }

    private void checkNotificationTables() {
        if (sessionFactory == null || descriptors.isEmpty()) {
            return;
        }

        final Session session = sessionFactory.openSession();
        checkTable(session, "notification_settings");
        checkTable(session, "notification_timestamp");
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
}
