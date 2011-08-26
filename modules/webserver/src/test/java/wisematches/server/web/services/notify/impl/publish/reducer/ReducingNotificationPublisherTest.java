package wisematches.server.web.services.notify.impl.publish.reducer;

import org.easymock.Capture;
import org.junit.Test;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.NotificationManager;
import wisematches.server.web.services.notify.NotificationMover;
import wisematches.server.web.services.notify.NotificationPublisher;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ReducingNotificationPublisherTest {
	public ReducingNotificationPublisherTest() {
	}

	@Test
	public void test() {
		final MemberPlayer player = new MemberPlayer(createMock(Account.class));
		final Map<String, Object> model = new HashMap<String, Object>();

		final NotificationDescription d1 = new NotificationDescription("game.started", null, "game", false, false);
		final NotificationDescription d2 = new NotificationDescription("game.finished", null, "game", false, true);
		final NotificationDescription d3 = new NotificationDescription("game.message", null, "message", false, false);

		final NotificationPublisher publisher = createStrictMock(NotificationPublisher.class);
		expect(publisher.raiseNotification("asd", player, NotificationMover.ACCOUNTS, model)).andReturn(null);
		expect(publisher.raiseNotification("game.started", player, NotificationMover.ACCOUNTS, model)).andReturn(null);
		expect(publisher.raiseNotification("game.finished", player, NotificationMover.ACCOUNTS, model)).andReturn(null);
		expect(publisher.raiseNotification("game.message", player, NotificationMover.ACCOUNTS, model)).andReturn(null);
		replay(publisher);

		final Capture<PlayerStateListener> listener = new Capture<PlayerStateListener>();

		final PlayerStateManager playerStateManager = createMock(PlayerStateManager.class);
		playerStateManager.addPlayerStateListener(capture(listener));
		expect(playerStateManager.isPlayerOnline(player)).andReturn(false);
		expect(playerStateManager.isPlayerOnline(player)).andReturn(true);
		expect(playerStateManager.isPlayerOnline(player)).andReturn(true);
		expect(playerStateManager.isPlayerOnline(player)).andReturn(true);
		expect(playerStateManager.isPlayerOnline(player)).andReturn(true);
		replay(playerStateManager);

		final NotificationManager notificationManager = createMock(NotificationManager.class);
		expect(notificationManager.getDescription("asd")).andReturn(null).anyTimes();
		expect(notificationManager.getDescription(d1.getName())).andReturn(d1).anyTimes();
		expect(notificationManager.isNotificationEnabled(d1, player)).andReturn(true).anyTimes();
		expect(notificationManager.getDescription(d2.getName())).andReturn(d2).anyTimes();
		expect(notificationManager.isNotificationEnabled(d2, player)).andReturn(true).anyTimes();
		expect(notificationManager.getDescription(d3.getName())).andReturn(d3).anyTimes();
		expect(notificationManager.isNotificationEnabled(d3, player)).andReturn(true).anyTimes();
		replay(notificationManager);

		final ReducingNotificationPublisher p = new ReducingNotificationPublisher();
		p.setNotificationPublisher(publisher);
		p.setPlayerStateManager(playerStateManager);
		p.setNotificationManager(notificationManager);

		p.raiseNotification("asd", player, NotificationMover.ACCOUNTS, model);
		p.raiseNotification("game.started", player, NotificationMover.ACCOUNTS, model);
		p.raiseNotification("game.finished", player, NotificationMover.ACCOUNTS, model);
		p.raiseNotification("game.message", player, NotificationMover.ACCOUNTS, model);
		p.raiseNotification("game.message", player, NotificationMover.ACCOUNTS, model);

		// check messages
		listener.getValue().playerOffline(player);

		// check clear
		p.raiseNotification("game.message", player, NotificationMover.ACCOUNTS, model);
		listener.getValue().playerAlive(player);
		listener.getValue().playerOffline(player);

		verify(publisher, playerStateManager, notificationManager);
	}
}
