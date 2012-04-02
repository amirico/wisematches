package wisematches.server.web.services.notify.publisher;

import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.publisher.NotificationOriginator;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * {@code NotificationPublisher} is base interface for interaction with players. It allows send localized
 * messages to a player using differ systems, like email or internal messages system.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationPublisher {
	/**
	 * Returns name of this publisher. More that one publishers can have the same name if they provide the same functionality.
	 *
	 * @return the publisher name
	 */
	String getPublisherName();

	/**
	 * Raise new notification and returns {@code Future} object that allows track notification state.
	 *
	 * @param code	the notification code.
	 * @param account the account who should receive notification
	 * @param originator   the original originator who initiated the notification
	 * @param model   additional model with required for transformation attributes
	 * @return the {@code Future} object that allows track notification or cancel it at all.
	 * @throws IllegalArgumentException if notification with specified {@code code} is unknown or can't be transformed because model doesn't have required attributes
	 * @throws NullPointerException	 if any {@code player}, {@code code} or {@code sender} is null.
	 */
	Future<Void> raiseNotification(String code, Account account, NotificationOriginator originator, Map<String, Object> model);

	/**
	 * Raise new notification and returns {@code Future} object that allows track notification state.
	 *
	 * @param code   the notification code.
	 * @param player the player who should receive notification
	 * @param originator  the original originator who initiated the notification
	 * @param model  additional model with required for transformation attributes
	 * @return the {@code Future} object that allows track notification or cancel it at all.
	 * @throws IllegalArgumentException if notification with specified {@code code} is unknown or can't be transformed because model doesn't have required attributes
	 * @throws NullPointerException	 if any {@code player}, {@code code} or {@code sender} is null.
	 */
	Future<Void> raiseNotification(String code, MemberPlayer player, NotificationOriginator originator, Map<String, Object> model);
}
