package wisematches.server.web.services.notice;

import wisematches.personality.Personality;

import java.util.Collection;

/**
 * This class allows manager notification and check they state.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationManager {
	/**
	 * Returns a notification description for specified name.
	 *
	 * @param name the notification name
	 * @return notification description or {@code null} if there is no notification with specified name.
	 */
	public NotificationDescription getDescription(String name);

	/**
	 * Returns unmodifiable collection of all known descriptions.
	 *
	 * @return the unmodifiable collection of all descriptions.
	 */
	public Collection<NotificationDescription> getDescriptions();

	/**
	 * Checks is notification with specified name enabled for specified player.
	 *
	 * @param name		the notification name
	 * @param personality the person who's notification should be checked.
	 * @return {@code true} if notification enabled; {@code false} - otherwise.
	 */
	boolean isNotificationEnabled(String name, Personality personality);

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
}
