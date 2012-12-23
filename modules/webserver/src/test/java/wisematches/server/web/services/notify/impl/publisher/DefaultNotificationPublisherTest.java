package wisematches.server.web.services.notify.impl.publisher;

import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountEditor;
import wisematches.server.web.services.notify.DeliveryException;
import wisematches.server.web.services.notify.NotificationMessage;
import wisematches.server.web.services.notify.NotificationPublisher;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.impl.delivery.NotificationDeliveryServiceImpl;
import wisematches.server.web.services.notify.impl.delivery.converter.NotificationConverter;
import wisematches.server.web.services.notify.publisher.impl.DefaultNotificationPublisher;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationPublisherTest {
    public DefaultNotificationPublisherTest() {
    }

    @Test
    public void test() throws DeliveryException {
        final Object context = new Object();
        final Account account = new AccountEditor("mock", "mock", "").createAccount();

        final NotificationDeliveryServiceImpl.NotificationOld notification = new NotificationDeliveryServiceImpl.NotificationOld("mock", "mockt", account, NotificationSender.GAME, context);
        final NotificationMessage msg = new NotificationMessage("mock", "mocks", "mockm", account, NotificationSender.GAME);

        final NotificationPublisher transport = createStrictMock(NotificationPublisher.class);
        transport.sendNotification(msg);
        replay(transport);

        final NotificationConverter transformer = createStrictMock(NotificationConverter.class);
        expect(transformer.createMessage(notification)).andReturn(msg);
        replay(transformer);

        final DefaultNotificationPublisher publisher = new DefaultNotificationPublisher();
        publisher.setTransport(transport);
        publisher.setTransformer(transformer);

        publisher.publishNotification(notification);

        verify(transport, transformer);

    }
}
