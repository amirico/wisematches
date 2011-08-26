package wisematches.server.web.services.notify.impl.publish.reducer;

import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationMover;
import wisematches.server.web.services.notify.NotificationPublisher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FilteringNotificationPublisherTest {
	public FilteringNotificationPublisherTest() {
	}

	@Test
	public void test() {
		final MemberPlayer player = new MemberPlayer(createMock(Account.class));
		final Map<String, Object> model = new HashMap<String, Object>();

		final NotificationPublisher publisher = createStrictMock(NotificationPublisher.class);

		final FilteringNotificationPublisher p = new FilteringNotificationPublisher();
		p.setNotificationPublisher(publisher);
		p.setAcceptedNotifications(new HashSet<String>(Arrays.asList("game.finished", "game.challenge")));

		expect(publisher.raiseNotification("game.finished", player, NotificationMover.ACCOUNTS, model)).andReturn(null);
		expect(publisher.raiseNotification("game.challenge", player, NotificationMover.ACCOUNTS, model)).andReturn(null);
		replay(publisher);

		p.raiseNotification("asd", player, NotificationMover.ACCOUNTS, model);
		p.raiseNotification("game.finished", player, NotificationMover.ACCOUNTS, model);
		p.raiseNotification("game.challenge", player, NotificationMover.ACCOUNTS, model);
		p.raiseNotification("qwe", player, NotificationMover.ACCOUNTS, model);
		verify(publisher);
	}
}
