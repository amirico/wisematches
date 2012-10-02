package wisematches.server.web.services.notify.impl.publisher;

import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.*;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationProcessorTest {
    public DefaultNotificationProcessorTest() {
    }

    @Test
    public void test() throws Exception {
        final Account account = createNiceMock(Account.class);
        final Notification template = new Notification("mock.code", "mock.template", account, NotificationSender.GAME);
        final NotificationMessage message = new NotificationMessage("mock.code", "mock.subject", "mock.message", account, NotificationSender.GAME);

        final NotificationTransport transport = createStrictMock(NotificationTransport.class);
        transport.sendNotification(message);
        replay(transport);

        final NotificationTransformer transformer = createStrictMock(NotificationTransformer.class);
        expect(transformer.createMessage(template)).andReturn(message);
        replay(transformer);

        final DefaultNotificationPublisher processor = new DefaultNotificationPublisher();
        processor.setTransport(transport);
        processor.setTransformer(transformer);

        processor.publishNotification(template);

        verify(transport, transformer);
    }
}
