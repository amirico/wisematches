package wisematches.server.web.services.notify.impl.transport;

import org.easymock.EasyMock;
import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.NotificationMessage;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

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

        final MessageNotificationTransport transport = new MessageNotificationTransport();
        transport.setMessageManager(manager);

        transport.sendNotification(new NotificationMessage("mock.code", "mock.subject", "mock.message", account, NotificationSender.GAME));

        verify(manager);
    }
}
