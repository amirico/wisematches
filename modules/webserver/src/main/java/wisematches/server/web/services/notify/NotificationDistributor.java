package wisematches.server.web.services.notify;

import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;

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
public interface NotificationDistributor {
    void raiseNotification(String code, Account recipient, NotificationCreator creator, Object context);

    void raiseNotification(String code, MemberPlayer recipient, NotificationCreator creator, Object context);
}
