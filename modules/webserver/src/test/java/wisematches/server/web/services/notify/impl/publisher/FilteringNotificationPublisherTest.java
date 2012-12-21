package wisematches.server.web.services.notify.impl.publisher;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationPublisherOld;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.impl.delivery.DefaultNotificationDeliveryService;
import wisematches.server.web.services.notify.publisher.impl.DefaultNotificationPublisher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FilteringNotificationPublisherTest {
	public FilteringNotificationPublisherTest() {
	}

	@Test
	public void testPublishNotification() throws Exception {
		final Account account = createMock(Account.class);
		final Map<String, Object> model = new HashMap<String, Object>();

		final Capture<DefaultNotificationDeliveryService.NotificationOld> capture = new Capture<DefaultNotificationDeliveryService.NotificationOld>(CaptureType.ALL);

		final NotificationPublisherOld publisher = createStrictMock(DefaultNotificationPublisher.class);
		expect(publisher.publishNotification(capture(capture))).andReturn(true);
		expect(publisher.publishNotification(capture(capture))).andReturn(false);
		replay(publisher);

		final FilteringNotificationPublisher p = new FilteringNotificationPublisher();
		p.setNotificationPublisher(publisher);
		p.setAllowedNotifications(new HashSet<String>(Arrays.asList("game.state.finished", "game.challenge.initiated")));

		assertFalse(p.publishNotification(new DefaultNotificationDeliveryService.NotificationOld("asd", "asd", account, NotificationSender.ACCOUNTS)));
		assertTrue(p.publishNotification(new DefaultNotificationDeliveryService.NotificationOld("game.state.finished", "asd", account, NotificationSender.ACCOUNTS)));
		assertFalse(p.publishNotification(new DefaultNotificationDeliveryService.NotificationOld("game.challenge.initiated", "asd", account, NotificationSender.ACCOUNTS, model)));
		assertFalse(p.publishNotification(new DefaultNotificationDeliveryService.NotificationOld("qwe", "asd", account, NotificationSender.ACCOUNTS)));

		assertTemplate("game.state.finished", "asd", account, NotificationSender.ACCOUNTS, null, capture.getValues().get(0));
		assertTemplate("game.challenge.initiated", "asd", account, NotificationSender.ACCOUNTS, model, capture.getValues().get(1));

		verify(publisher);
	}

	private void assertTemplate(String code, String template, Account account, NotificationSender sender, Object model, DefaultNotificationDeliveryService.NotificationOld notification) {
		assertEquals(code, notification.getCode());
		assertEquals(template, notification.getTemplate());
		assertEquals(account, notification.getRecipient());
		assertEquals(sender, notification.getSender());
		assertEquals(model, notification.getContext());
	}
}
