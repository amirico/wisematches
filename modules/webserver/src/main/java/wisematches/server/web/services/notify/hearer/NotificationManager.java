package wisematches.server.web.services.notify.hearer;

import wisematches.personality.Personality;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.NotificationDescriptor;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationManager {
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
	 * @param code		the code of a notification
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
	NotificationMask getNotificationMask(Personality personality);

	/**
	 * Updates notifications mask for specified player.
	 *
	 * @param personality  the player who's notifications mask should be updated.
	 * @param notification the new notification's mask.
	 */
	void setNotificationMask(Personality personality, NotificationMask notification);


	/**
	 * Returns a notification description for specified name.
	 *
	 * @param code the notification name
	 * @return notification description or {@code null} if there is no notification with specified name.
	 */
	@Deprecated
	NotificationDescription getDescription(String code);

	/**
	 * Returns unmodifiable collection of all known descriptions.
	 *
	 * @return the unmodifiable collection of all descriptions.
	 */
	@Deprecated
	Collection<NotificationDescription> getDescriptions();

	/**
	 * Checks is notification with specified name enabled for specified player.
	 *
	 * @param name		the notification name
	 * @param personality the person who's notification should be checked.
	 * @return {@code true} if notification enabled; {@code false} - otherwise.
	 */
	@Deprecated
	boolean isNotificationEnabled(NotificationDescription name, Personality personality);
}
