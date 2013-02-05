package wisematches.server.services.notify.impl.publisher;

import org.junit.Test;
import wisematches.core.Player;
import wisematches.server.services.message.MessageManager;
import wisematches.server.services.notify.Notification;
import wisematches.server.services.notify.NotificationSender;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationPublisherTest {
	public MessageNotificationPublisherTest() {
	}

	@Test
	public void testSendNotification() {
		final Player account = createNiceMock(Player.class);

		final MessageManager manager = createStrictMock(MessageManager.class);
		manager.sendNotification(account, "mock.message", true);
		replay(manager);

		final MessageNotificationPublisher transport = new MessageNotificationPublisher();
		transport.setMessageManager(manager);

		transport.publishNotification(new Notification(1, "mock.code", "mock.subject", "mock.message", account, NotificationSender.GAME));

		verify(manager);
	}
}
