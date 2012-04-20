package wisematches.server.web.services.notify;

import wisematches.personality.Personality;

import java.util.Collection;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationManager {
	void addNotificationManagerListener(NotificationManagerListener l);

	void removeNotificationManagerListener(NotificationManagerListener l);


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
	 * Returns date when last notification was sent.
	 *
	 * @param personality the personality who's info should be returned.
	 * @param code        the notification code
	 * @return data when notification was sent last time or {@code null} if notification wasn't sent.
	 */
	Date getNotificationDate(Personality personality, String code);

	/**
	 * Checks is notification with specified code enabled for specified player or not. If
	 * there is no notification with specified code when {@code false} will be returned.
	 *
	 * @param personality the player who should be checked
	 * @param code        the code of a notification
	 * @return {@code true} if notification is enabled for the player; {@code false} if
	 *         notification disabled or unknown.
	 */
	boolean isNotificationEnabled(Personality personality, String code);


	/**
	 * Returns notification mask for specified personality.
	 *
	 * @param personality the personality who's mask should be returned.
	 * @return the notification's mask.
	 */
	NotificationSettings getNotificationCondition(Personality personality);

	/**
	 * Updates notifications mask for specified player.
	 *
	 * @param personality the player who's notifications mask should be updated.
	 * @param conditions  the new notification's mask.
	 */
	void setNotificationCondition(Personality personality, NotificationSettings conditions);
}
