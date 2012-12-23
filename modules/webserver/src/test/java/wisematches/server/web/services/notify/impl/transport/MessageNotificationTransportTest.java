package wisematches.server.web.services.notify.impl.transport;

import org.easymock.EasyMock;
import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notify.NotificationMessage;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.impl.publisher.MessageNotificationPublisher;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationTransportTest {
    public MessageNotificationTransportTest() {
    }

    @Test
    public void testSendNotification() {
        final Account account = EasyMock.createNiceMock(Account.class);

        final MessageManager manager = createStrictMock(MessageManager.class);
        manager.sendNotification(account, "mock.message", true);
        replay(manager);

        final MessageNotificationPublisher transport = new MessageNotificationPublisher();
        transport.setMessageManager(manager);

        transport.sendNotification(new NotificationMessage("mock.code", "mock.subject", "mock.message", account, NotificationSender.GAME));

        verify(manager);
    }
}
