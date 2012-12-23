package wisematches.server.web.services.notify.impl.settings;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.*;
import wisematches.server.web.services.notify.impl.NotificationManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SQLNotificationSettingsManager implements NotificationSettingsManager {
    private NotificationManager notificationManager;

    private final Collection<NotificationSettingsListener> listeners = new CopyOnWriteArraySet<>();

    public SQLNotificationSettingsManager() {
    }

    @Override
    public void addNotificationManagerListener(NotificationSettingsListener l) {
        if (l != null) {
            listeners.add(l);
        }
    }

    @Override
    public void removeNotificationManagerListener(NotificationSettingsListener l) {
        listeners.remove(l);
    }

    @Override
    public NotificationDescriptor getDescriptor(String code) {
        return notificationManager.getDescriptor(code);
    }

    @Override
    public Collection<NotificationDescriptor> getDescriptors() {
        return notificationManager.getDescriptors();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NotificationScope getNotificationScope(Personality personality, String code) {
        return notificationManager.getNotificationScope(personality, code);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NotificationSettings getNotificationSettings(Personality personality) {
        return notificationManager.getNotificationSettings(personality);
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY, readOnly = false)
    public void setNotificationSettings(Personality personality, NotificationSettings settings) {
        final NotificationSettings oldSettings = notificationManager.updateNotificationSettings(personality, settings);

        for (NotificationSettingsListener listener : listeners) {
            listener.notificationConditionChanged(personality, oldSettings, settings);
        }
    }
}
