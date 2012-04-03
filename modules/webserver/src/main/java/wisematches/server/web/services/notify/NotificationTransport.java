package wisematches.server.web.services.notify;

import java.io.IOException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationTransport {
    void sendNotification(NotificationMessage message) throws IOException;
}
