package wisematches.server.web.services.notify.impl;

import wisematches.core.Player;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.TransformationException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationConverter {
	Notification createNotification(String code, Player target, NotificationSender sender, Object context) throws TransformationException;
}
