package wisematches.server.web.services.notify;

import wisematches.personality.Personality;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationSettingsManager {
    void addNotificationManagerListener(NotificationSettingsListener l);

    void removeNotificationManagerListener(NotificationSettingsListener l);


    /**
     * Returns a notification description for specified name.
     *
     * @param code the notification name
     * @return notification description or {@code null} if there is no notification with specified name.
     */
    NotificationDescriptor getDescriptor(String code);

    /**
     * Returns unmodifiable collection of all known descriptions.
     *
     * @return the unmodifiable collection of all descriptions.
     */
    Collection<NotificationDescriptor> getDescriptors();


    /**
     * Checks is notification with specified code enabled for specified player or not. If
     * there is no notification with specified code when {@code false} will be returned.
     *
     * @param personality the player who should be checked
     * @param code        the code of a notification
     * @return {@code true} if notification is enabled for the player; {@code false} if
     *         notification disabled or unknown.
     */
    NotificationScope getNotificationScope(Personality personality, String code);


    /**
     * Returns notification mask for specified personality.
     *
     * @param personality the personality who's mask should be returned.
     * @return the notification's mask.
     */
    NotificationSettings getNotificationSettings(Personality personality);

    /**
     * Updates notifications mask for specified player.
     *
     * @param personality the player who's notifications mask should be updated.
     * @param settings    the new notification's mask.
     */
    void setNotificationSettings(Personality personality, NotificationSettings settings);
}
