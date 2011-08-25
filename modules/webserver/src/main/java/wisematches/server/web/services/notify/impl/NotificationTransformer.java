package wisematches.server.web.services.notify.impl;

import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationMover;
import wisematches.server.web.services.notify.NotificationPublisher;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationTransformer {
	Notification createNotification(String code, MemberPlayer player, NotificationMover mover, NotificationPublisher publisher, Map<String, Object> model) throws Exception;
}