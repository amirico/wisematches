package wisematches.server.services.notify;

import wisematches.core.Personality;
import wisematches.core.Player;

import java.util.Set;

/**
 * {@code NotificationDistributor} is main notification distribution interface. It prepares
 * notification, check is it should be processed and so on and sends to all known publishers.
 * <p/>
 * The {@code NotificationDistributor} is always asynchronous. There is no warranty that notification was sent
 * after method invoking.
 * <p/>
 * There is no way to track notification errors. If you need to be sure that notification/email was sent please
 * use appropriate {@code NotificationPublisher}.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationService {
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
	 * @return default descriptor for specified notification.
	 * @throws IllegalArgumentException if there is no notification with specified code.
	 */
	NotificationDescriptor getDescriptor(String code);

	/**
	 * Returns personal descriptor for specified player and notification code.
	 *
	 * @param code        notification code
	 * @param personality personality who's descriptor should be returned.
	 * @return personal descriptor for notification of default descriptor if personality doesn't have personal descriptor.
	 * @throws IllegalArgumentException if there is no notification with specified code.
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


	Notification raiseNotification(String code, Player target, NotificationSender sender, Object context) throws NotificationException;
}