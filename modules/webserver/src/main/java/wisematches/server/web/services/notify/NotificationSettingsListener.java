package wisematches.server.web.services.notify;

import wisematches.personality.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationSettingsListener {
	void notificationConditionChanged(Personality personality, NotificationSettings oldConditions, NotificationSettings newConditions);
}
