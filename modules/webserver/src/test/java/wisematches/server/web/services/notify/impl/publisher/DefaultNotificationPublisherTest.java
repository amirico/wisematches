package wisematches.server.web.services.notify.impl.publisher;

import org.easymock.EasyMock;
import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountEditor;
import wisematches.server.web.services.notify.*;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationPublisherTest {
	public DefaultNotificationPublisherTest() {
	}

	@Test
	public void test() throws PublicationException {
		final Object context = new Object();
		final Account account = new AccountEditor("mock", "mock", "").createAccount();

		final Notification notification = new Notification("mock", "mockt", account, NotificationSender.GAME, context);
		final NotificationMessage msg = new NotificationMessage("mock", "mocks", "mockm", account, NotificationSender.GAME);

		final NotificationTransport transport = createStrictMock(NotificationTransport.class);
		transport.sendNotification(msg);
		replay(transport);

		final NotificationTransformer transformer = createStrictMock(NotificationTransformer.class);
		expect(transformer.createMessage(notification)).andReturn(msg);
		replay(transformer);

		final DefaultNotificationPublisher publisher = new DefaultNotificationPublisher();
		publisher.setTransport(transport);
		publisher.setTransformer(transformer);

		publisher.publishNotification(notification);

		verify(transport, transformer);

	}
}
