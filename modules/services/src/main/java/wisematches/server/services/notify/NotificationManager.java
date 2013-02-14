package wisematches.server.services.notify;

import wisematches.core.Personality;

import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationManager {
	/**
	 * Returns unmodifiable collection of all known notification codes.
	 *
	 * @return unmodifiable collection of all known notification codes.
	 */
	Set<String> getNotificationCodes();

	/**
	 * Returns default descriptor for specified notification.
	 *
	 * @param code notification code
	 * @return default descriptor for specified notification or {@code null} if there is no notification with specified code.
	 */
	NotificationDescriptor getDescriptor(String code);

	/**
	 * Returns personal descriptor for specified player and notification code.
	 *
	 * @param code        notification code
	 * @param personality personality who's descriptor should be returned.
	 * @return personal descriptor for notification of default descriptor if personality doesn't have personal
	 *         descriptor or {@code null} if there is no notification with specified code.
	 */
	NotificationScope getNotificationScope(String code, Personality personality);

	/**
	 * Changes personal descriptor for notification.
	 *
	 * @param code        notification code
	 * @param personality player who's descriptor should be updated.
	 * @param scope       personal descriptor for notification.
	 * @return previous personal descriptor or {@code null} if player don't have previous personal descriptor
	 */
	NotificationScope setNotificationScope(String code, Personality personality, NotificationScope scope);
}
