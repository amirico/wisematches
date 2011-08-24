package wisematches.server.web.services.notify;

import wisematches.personality.player.member.MemberPlayer;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationPublisher {
	/**
	 * Raise new notification and returns {@code Future} object that allows track notification state.
	 *
	 * @param player the player who should receive notification
	 * @param code   the notification code.
	 * @param sender the original sender who initiated the notification
	 * @param model  additional model with required for transformation attributes
	 * @return the {@code Future} object that allows track notification or cancel it at all.
	 * @throws IllegalArgumentException if notification with specified {@code code} is unknown or can't be transformed because model doesn't have required attributes
	 * @throws NullPointerException	 if any {@code player}, {@code code} or {@code sender} is null.
	 */
	Future<Void> raiseNotification(MemberPlayer player, String code, NotificationSender sender, Map<String, Object> model);
}
