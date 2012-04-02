package wisematches.server.web.services.notify.distributor;

import org.springframework.scheduling.TaskScheduler;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationDistributor;
import wisematches.server.web.services.notify.manager.NotificationCondition;
import wisematches.server.web.services.notify.manager.NotificationManager;
import wisematches.server.web.services.notify.publisher.NotificationPublisher;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationDistributor implements NotificationDistributor {
    private TaskScheduler taskScheduler;
    private NotificationManager notificationManager;
    private Collection<NotificationPublisher> notificationPublishers;

    public DefaultNotificationDistributor() {
    }

    @Override
    public Future<Void> raiseNotification(String code, Account recipient, NotificationCreator creator, Object context) {
        final NotificationDescriptor descriptor = notificationManager.getDescriptor(code);
        if (descriptor == null) {
            throw new IllegalArgumentException("There is no notification with code " + code);
        }

        final NotificationCondition condition = notificationManager.getNotificationCondition(recipient);
        if (!condition.isEnabled(code)) {
        }
        return null;
    }

    @Override
    public Future<Void> raiseNotification(String code, MemberPlayer recipient, NotificationCreator creator, Object context) {
        return raiseNotification(code, recipient.getAccount(), creator, context);
    }

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public void setNotificationPublishers(Collection<NotificationPublisher> notificationPublishers) {
        this.notificationPublishers = notificationPublishers;
    }
}
