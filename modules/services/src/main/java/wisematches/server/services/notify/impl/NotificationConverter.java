package wisematches.server.services.notify.impl;

import wisematches.core.Player;
import wisematches.server.services.notify.Notification;
import wisematches.server.services.notify.NotificationSender;
import wisematches.server.services.notify.TransformationException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationConverter {
	Notification createNotification(String code, Player target, NotificationSender sender, Object context) throws TransformationException;
}
