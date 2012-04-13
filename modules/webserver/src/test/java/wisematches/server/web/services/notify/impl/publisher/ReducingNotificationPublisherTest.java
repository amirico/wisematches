package wisematches.server.web.services.notify.impl.publisher;

import org.easymock.Capture;
import org.easymock.IMockBuilder;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationPublisher;
import wisematches.server.web.services.notify.NotificationTemplate;
import wisematches.server.web.services.notify.PublicationException;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ReducingNotificationPublisherTest {
	public ReducingNotificationPublisherTest() {
	}

	@Test
	public void testPublishNotification() throws PublicationException {
		final Account P1 = mockAccount(1);

		final NotificationTemplate TA11 = new NotificationTemplate("n.a.1", P1, NotificationCreator.GAME);
		final NotificationTemplate TA21 = new NotificationTemplate("n.a.2", P1, NotificationCreator.GAME);
		final NotificationTemplate TB11 = new NotificationTemplate("n.b.1", P1, NotificationCreator.GAME);
		final NotificationTemplate TB21 = new NotificationTemplate("n.b.2", P1, NotificationCreator.GAME);

		final Map<String, String> groups = new HashMap<String, String>();
		groups.put("n.a.1", "n.a");
		groups.put("n.a.2", "n.a");

		final HashSet<String> states = new HashSet<String>();
		states.add("n.a.2");
		states.add("n.b.1");

		final Capture<PlayerStateListener> stateListenerCapture = new Capture<PlayerStateListener>();

		final PlayerStateManager playerStateManager = createStrictMock(PlayerStateManager.class);
		playerStateManager.addPlayerStateListener(capture(stateListenerCapture));
		expect(playerStateManager.isPlayerOnline(P1)).andReturn(false);
		expect(playerStateManager.isPlayerOnline(P1)).andReturn(false);
		expect(playerStateManager.isPlayerOnline(P1)).andReturn(false);
		replay(playerStateManager);

		final NotificationPublisher originalPublisher = createStrictMock(NotificationPublisher.class);
		expect(originalPublisher.publishNotification(TB11)).andReturn(true);
		expect(originalPublisher.publishNotification(TA21)).andReturn(true);
		replay(originalPublisher);

		final ReducingNotificationPublisher publisher = new ReducingNotificationPublisher();
		publisher.setGroupedNotifications(groups);
		publisher.setStateIndependentNotifications(states);
		publisher.setNotificationPublisher(originalPublisher);
		publisher.setPlayerStateManager(playerStateManager);
		publisher.setTaskExecutor(new ConcurrentTaskExecutor());

		publisher.publishNotification(TB11); // not grouped
		publisher.publishNotification(TA11); // first in group, wasn't sent
		publisher.publishNotification(TA21); // send in group, must be ignored

		verify(originalPublisher, playerStateManager);
	}

	private Account mockAccount(final long id) {
		IMockBuilder<Account> mockBuilder = createMockBuilder(Account.class);
		mockBuilder.withConstructor(id);
		return mockBuilder.createMock("MockAccount_" + id);
	}

/*
    @Test
    public void test() {
        final Account account = createMock(Account.class);
        final MemberPlayer player = new MemberPlayer(account);
        final Map<String, Object> model = new HashMap<String, Object>();

        final NotificationDescription d1 = new NotificationDescription("game.state.started", null, "game", false, false);
        final NotificationDescription d2 = new NotificationDescription("game.state.finished", null, "game", false, true);
        final NotificationDescription d3 = new NotificationDescription("game.message", null, "message", false, false);

        final NotificationPublisher publisher = createStrictMock(DefaultNotificationPublisher.class);
        expect(publisher.raiseNotification("asd", account, NotificationCreator.ACCOUNTS, model)).andReturn(null);
        expect(publisher.raiseNotification("game.state.started", account, NotificationCreator.ACCOUNTS, model)).andReturn(null);
        expect(publisher.raiseNotification("game.state.finished", account, NotificationCreator.ACCOUNTS, model)).andReturn(null);
        expect(publisher.raiseNotification("game.message", account, NotificationCreator.ACCOUNTS, model)).andReturn(null);
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
        p.setNotificationDistributor(publisher);
        p.setPlayerStateManager(playerStateManager);
        p.setNotificationManager(notificationManager);

        p.raiseNotification("asd", player, NotificationCreator.ACCOUNTS, model);
        p.raiseNotification("game.state.started", player, NotificationCreator.ACCOUNTS, model);
        p.raiseNotification("game.state.finished", player, NotificationCreator.ACCOUNTS, model);
        p.raiseNotification("game.message", player, NotificationCreator.ACCOUNTS, model);
        p.raiseNotification("game.message", player, NotificationCreator.ACCOUNTS, model);

        // check messages
        listener.getValue().playerOffline(player);

        // check clear
        p.raiseNotification("game.message", player, NotificationCreator.ACCOUNTS, model);
        listener.getValue().playerAlive(player);
        listener.getValue().playerOffline(player);

        verify(publisher, playerStateManager, notificationManager);
    }
*/
}
