package wisematches.server.web.services.notify;

/**
 * The {@code NotificationPublisher} does template transformation and publish message to appropriate
 * transport.
 * <p/>
 * There are two possible ways to publish notification: {@code unreliable} - it's asynchronous way but
 * there are no warranties that notification will be processed; {@code reliable} - synchronous version
 * and thread will be blocked till notification processing.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationPublisher {
    /**
     * Publishes a notification by specified template and wait response from transport system. The
     * method can provide some reliables but if transport is asynchronous you can't be sure
     * that notification was really sent.
     *
     * @param template the notification template to be published.
     * @return {@code true} if notification was sent; {@code false} if it was rejected.
     * @throws PublicationException if notification can't be sent.
     */
    boolean publishNotification(NotificationTemplate template) throws PublicationException;
}
