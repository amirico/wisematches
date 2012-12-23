package wisematches.server.web.services.notify.impl.delivery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.task.TransactionalTaskExecutor;
import wisematches.server.web.services.notify.*;
import wisematches.server.web.services.notify.impl.NotificationManager;
import wisematches.server.web.services.notify.impl.delivery.converter.NotificationConverter;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationDeliveryServiceImpl implements NotificationDeliveryService {
    private PlayerStateManager playerStateManager;
    private TransactionalTaskExecutor taskExecutor;
    private NotificationManager notificationManager;
    private NotificationSettingsManager settingsManager;
    private NotificationConverter notificationConverter;
    private Collection<NotificationPublisher> notificationPublishers = new ArrayList<>();

    private final Lock lock = new ReentrantLock();
    private final ThePlayerStateListener stateListener = new ThePlayerStateListener();

    private final Set<String> redundantNotifications = new HashSet<>();
    private final Set<String> mandatoryNotifications = new HashSet<>();
    private final Map<Personality, Collection<TrackingNotificationMessage>> waitingNotifications = new HashMap<>();

    private static final EnumSet<NotificationScope> INTERNAL_SCOPE = EnumSet.of(NotificationScope.INTERNAL, NotificationScope.GLOBAL);
    private static final EnumSet<NotificationScope> EXTERNAL_SCOPE = EnumSet.of(NotificationScope.EXTERNAL, NotificationScope.GLOBAL);

    private static final Log log = LogFactory.getLog("wisematches.server.notify.distributor");

    public NotificationDeliveryServiceImpl() {
    }

    @Override
    public NotificationPublisher getNotificationPublisher(String name) {
        for (NotificationPublisher publisher : notificationPublishers) {
            if (publisher.getName().equals(name)) {
                return publisher;
            }
        }
        return null;
    }

    @Override
    public Collection<NotificationPublisher> getNotificationPublishers() {
        return Collections.unmodifiableCollection(notificationPublishers);
    }

    @Override
    public Collection<NotificationPublisher> getNotificationPublishers(NotificationScope scope) {
        if (scope == NotificationScope.GLOBAL) {
            return getNotificationPublishers();
        } else {
            final Collection<NotificationPublisher> res = new ArrayList<>(notificationPublishers.size());
            for (NotificationPublisher publisher : notificationPublishers) {
                if (publisher.getNotificationScope() == scope) {
                    res.add(publisher);
                }
            }
            return res;
        }
    }

    @Override
    public void raiseNotification(String code, MemberPlayer recipient, NotificationSender sender, Object context) {
        raiseNotification(code, recipient.getAccount(), sender, context);
    }

    @Override
    public void raiseNotification(String code, Account recipient, NotificationSender sender, Object context) {
        raiseNotification(code, recipient, sender, context, null);
    }

    @Override
    public void raiseNotification(String code, MemberPlayer recipient, NotificationSender sender, Object context, DeliveryCallback callback) {
        raiseNotification(code, recipient.getAccount(), sender, context, null);
    }

    @Override
    public void raiseNotification(final String code, final Account recipient, final NotificationSender sender, final Object context, final DeliveryCallback callback) {
        final NotificationDescriptor descriptor = settingsManager.getDescriptor(code);
        if (descriptor == null) {
            throw new IllegalArgumentException("There is no notification with code " + code);
        }
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    final TrackingNotificationMessage message = notificationConverter.createMessage(descriptor, recipient, sender, context, callback);
                    processNotification(message, descriptor);
                } catch (TransformationException ex) {
                    log.error("Notification can't be transformed correctly", ex);
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    protected void processNotification(TrackingNotificationMessage notification, NotificationDescriptor descriptor) {
        publishNotification(notification, INTERNAL_SCOPE);

        final String code = notification.getCode();
        final Personality recipient = notification.getAccount();
        if (settingsManager.getNotificationScope(recipient, code)) {
            if (playerStateManager.isPlayerOnline(recipient)) {
                if (mandatoryNotifications.contains(code)) {
                    publishNotification(notification, EXTERNAL_SCOPE);
                } else {
                    if (redundantNotifications.contains(code)) {
                        final Collection<TrackingNotificationMessage> notifications = waitingNotifications.get(recipient);
                        if (notifications != null) {
                            for (TrackingNotificationMessage n : notifications) {
                                if (code.equals(n.getCode())) {
                                    return; // ignore
                                }
                            }
                        }
                        postponeNotification(notification);
                    } else {
                        postponeNotification(notification);
                    }
                }
            } else {
                if (redundantNotifications.contains(code)) {
                    final Date activityDate = playerStateManager.getLastActivityDate(recipient);
                    final Date notificationDate = notificationManager.getNotificationDate(recipient, code);
                    if (notificationDate == null || (activityDate != null && notificationDate.before(activityDate))) {
                        publishNotification(notification, EXTERNAL_SCOPE);
                    }
                    // ignore
                } else {
                    publishNotification(notification, EXTERNAL_SCOPE);
                }
            }
        } else {
            log.debug("Notification '" + code + "' was ignored: disabled by player");
        }
    }

    private void postponeNotification(final TrackingNotificationMessage notification) {
        final Account account = notification.getAccount();
        Collection<TrackingNotificationMessage> notifications = waitingNotifications.get(account);
        if (notifications == null) {
            notifications = new ArrayList<>();
            waitingNotifications.put(account, notifications);
        }
        notifications.add(notification);
    }

    private void processUnpublishedNotifications(final Personality person) {
        final Collection<TrackingNotificationMessage> notifications = waitingNotifications.remove(person);
        if (notifications != null) {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    for (TrackingNotificationMessage notification : notifications) {
                        publishNotification(notification, EXTERNAL_SCOPE);
                    }
                }
            });
        }
    }

    private void publishNotification(final TrackingNotificationMessage notification, final EnumSet<NotificationScope> scopes) {
        final DeliveryCallback deliveryCallback = notification.getDeliveryCallback();

        for (NotificationPublisher publisher : notificationPublishers) {
            if (scopes.contains(publisher.getNotificationScope())) {
                try {
                    publisher.sendNotification(notification);
                    if (deliveryCallback != null) {
                        deliveryCallback.notificationPublished(notification);
                    }
                } catch (PublicationException ex) {
                    log.error("Notification can't be delivered correctly", ex);
                    if (deliveryCallback != null) {
                        deliveryCallback.notificationRejected(notification, publisher, ex);
                    }
                }
            }
        }
    }

    public void setTaskExecutor(TransactionalTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void setPlayerStateManager(PlayerStateManager playerStateManager) {
        if (this.playerStateManager != null) {
            this.playerStateManager.removePlayerStateListener(stateListener);
        }

        this.playerStateManager = playerStateManager;

        if (this.playerStateManager != null) {
            this.playerStateManager.addPlayerStateListener(stateListener);
        }
    }

    public void setNotificationConverter(NotificationConverter notificationConverter) {
        this.notificationConverter = notificationConverter;
    }

    public void setNotificationPublishers(Collection<NotificationPublisher> notificationPublishers) {
        this.notificationPublishers.clear();

        if (notificationPublishers != null) {
            this.notificationPublishers.addAll(notificationPublishers);
        }
    }

    public void setMandatoryNotifications(Set<String> mandatoryNotifications) {
        this.mandatoryNotifications.clear();

        if (mandatoryNotifications != null) {
            this.mandatoryNotifications.addAll(mandatoryNotifications);
        }
    }

    public void setRedundantNotifications(Set<String> redundantNotifications) {
        this.redundantNotifications.clear();

        if (redundantNotifications != null) {
            this.redundantNotifications.addAll(redundantNotifications);
        }
    }

    public void setSettingsManager(NotificationSettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    private class ThePlayerStateListener implements PlayerStateListener {
        private ThePlayerStateListener() {
        }

        @Override
        public void playerOnline(Personality person) {
            playerAlive(person);
        }

        @Override
        public void playerAlive(Personality person) {
            lock.lock();
            try {
                waitingNotifications.remove(person);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void playerOffline(Personality person) {
            lock.lock();
            try {
                processUnpublishedNotifications(person);
            } finally {
                lock.unlock();
            }
        }
    }
}
