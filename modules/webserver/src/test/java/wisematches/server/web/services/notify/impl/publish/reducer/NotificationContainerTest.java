package wisematches.server.web.services.notify.impl.publish.reducer;

import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.NotificationMover;

import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationContainerTest {
	public NotificationContainerTest() {
	}

	@Test
	public void test() {
		final Account account = createMock(Account.class);

		final NotificationDescription d1 = new NotificationDescription("game.test", null, null, false, false);
		final NotificationDescription d2 = new NotificationDescription("game.started", null, "game", false, false);
		final NotificationDescription d3 = new NotificationDescription("game.finished", null, "game", false, false);
		final NotificationDescription d4 = new NotificationDescription("game.message", null, "message", false, false);

		final NotificationContainer container = new NotificationContainer();
		container.addNotification(new NotificationInfo(account, NotificationMover.ACCOUNTS, d1, null));
		container.addNotification(new NotificationInfo(account, NotificationMover.ACCOUNTS, d2, null));
		container.addNotification(new NotificationInfo(account, NotificationMover.ACCOUNTS, d3, null));
		container.addNotification(new NotificationInfo(account, NotificationMover.ACCOUNTS, d4, null));
		container.addNotification(new NotificationInfo(account, NotificationMover.ACCOUNTS, d4, null));
		container.addNotification(new NotificationInfo(account, NotificationMover.ACCOUNTS, d4, null));

		final List<NotificationInfo> notifications = container.getNotifications();
		assertEquals(3, notifications.size());
		assertEquals("game.test", notifications.get(0).description.getName());
		assertEquals("game.started", notifications.get(1).description.getName());
		assertEquals("game.message", notifications.get(2).description.getName());
	}
}
