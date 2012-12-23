package wisematches.server.web.services.notify.impl;

import wisematches.personality.Personality;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationScope;
import wisematches.server.web.services.notify.NotificationSettings;

import java.util.Collection;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationManager {
    NotificationDescriptor getDescriptor(String code);

    Collection<NotificationDescriptor> getDescriptors();


    Date getNotificationDate(Personality personality, String code);

    void updateNotificationDate(Personality personality, String code);


    NotificationScope getNotificationScope(Personality personality, String code);


    NotificationSettings getNotificationSettings(Personality personality);

    NotificationSettings updateNotificationSettings(Personality personality, NotificationSettings settings);
}
