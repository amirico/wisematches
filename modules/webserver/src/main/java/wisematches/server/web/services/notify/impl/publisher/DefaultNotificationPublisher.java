package wisematches.server.web.services.notify.impl.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.*;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationPublisher implements NotificationPublisher {
    private SchedulingTaskExecutor taskExecutor;
    private NotificationManager notificationManager;
    private TransactionTemplate transactionTemplate;
    private Collection<NotificationProcessor> notificationProcessors;

    private static final Log log = LogFactory.getLog("wisematches.server.notify.distributor");

    public DefaultNotificationPublisher() {
    }

    @Override
    public Future<?> raiseNotification(final String code, final Account recipient, final NotificationCreator creator, final Object context) {
        final NotificationDescriptor descriptor = notificationManager.getDescriptor(code);
        if (descriptor == null) {
            throw new IllegalArgumentException("There is no notification with code " + code);
        }
        return taskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        processNotification(descriptor, recipient, creator, context);
                    }
                });
            }
        });
    }

    private void processNotification(NotificationDescriptor descriptor, Account recipient, NotificationCreator creator, Object context) {
        final String code = descriptor.getCode();
        final String template = descriptor.getTemplate();
        for (NotificationProcessor publisher : notificationProcessors) {
            if (publisher.isManageable() && !notificationManager.isNotificationEnabled(recipient, code)) {
                log.debug("Notification was ignored: disabled by player");
                continue;
            }

            try {
                publisher.publishNotification(new NotificationTemplate(code, template, recipient, creator, context));
            } catch (Exception e) {
                log.error("Notification can't be sent: code - " + code + ", publisher - " + publisher);
            }
        }
    }

    @Override
    public Future<?> raiseNotification(String code, MemberPlayer recipient, NotificationCreator creator, Object context) {
        return raiseNotification(code, recipient.getAccount(), creator, context);
    }


    public void setTaskExecutor(SchedulingTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        if (transactionManager == null) {
            transactionTemplate = null;
        } else {
            transactionTemplate = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
        }
    }

    public void setNotificationProcessors(Collection<NotificationProcessor> notificationProcessors) {
        this.notificationProcessors = notificationProcessors;
    }
}
