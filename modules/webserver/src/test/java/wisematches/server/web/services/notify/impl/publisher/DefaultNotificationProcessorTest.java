package wisematches.server.web.services.notify.impl.publisher;

import org.junit.Test;
import wisematches.personality.account.Account;
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
public class DefaultNotificationProcessorTest {
    public DefaultNotificationProcessorTest() {
    }

    @Test
    public void test() throws Exception {
        final Account account = createNiceMock(Account.class);
        final NotificationDeliveryServiceImpl.NotificationOld template = new NotificationDeliveryServiceImpl.NotificationOld("mock.code", "mock.template", account, NotificationSender.GAME);
        final NotificationMessage message = new NotificationMessage("mock.code", "mock.subject", "mock.message", account, NotificationSender.GAME);

        final NotificationPublisher transport = createStrictMock(NotificationPublisher.class);
        transport.sendNotification(message);
        replay(transport);

        final NotificationConverter transformer = createStrictMock(NotificationConverter.class);
        expect(transformer.createMessage(template)).andReturn(message);
        replay(transformer);

        final DefaultNotificationPublisher processor = new DefaultNotificationPublisher();
        processor.setTransport(transport);
        processor.setTransformer(transformer);

        processor.publishNotification(template);

        verify(transport, transformer);
    }
}
