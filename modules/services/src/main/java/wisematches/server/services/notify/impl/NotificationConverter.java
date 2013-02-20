package wisematches.server.services.notify.impl;

import wisematches.core.Member;
import wisematches.server.services.notify.Notification;
import wisematches.server.services.notify.NotificationSender;
import wisematches.server.services.notify.TransformationException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationConverter {
	Notification createNotification(String code, Member target, NotificationSender sender, Object context) throws TransformationException;
}
