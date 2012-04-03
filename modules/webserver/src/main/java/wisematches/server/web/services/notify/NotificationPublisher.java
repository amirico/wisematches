package wisematches.server.web.services.notify;

import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;

import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationPublisher {
	Future<?> raiseNotification(String code, Account recipient, NotificationCreator creator, Object context);

	Future<?> raiseNotification(String code, MemberPlayer recipient, NotificationCreator creator, Object context);
}
