package wisematches.server.web.services.notify.impl.publisher;

import org.easymock.EasyMock;
import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationSender;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationPublisherTest {
	public MessageNotificationPublisherTest() {
	}

	@Test
	public void testSendNotification() {
		final Account account = EasyMock.createNiceMock(Account.class);

		final MessageManager manager = createStrictMock(MessageManager.class);
		manager.sendNotification(account, "mock.message", true);
		replay(manager);

		final MessageNotificationPublisher transport = new MessageNotificationPublisher();
		transport.setMessageManager(manager);

		transport.publishNotification(new Notification(1, "mock.code", "mock.subject", "mock.message", account, NotificationSender.GAME));

		verify(manager);
	}
}
