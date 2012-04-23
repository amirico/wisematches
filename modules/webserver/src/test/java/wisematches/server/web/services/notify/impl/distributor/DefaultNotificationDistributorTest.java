package wisematches.server.web.services.notify.impl.distributor;

import org.easymock.Capture;
import org.junit.Test;
import org.springframework.core.task.SyncTaskExecutor;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.*;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationDistributorTest {
	public DefaultNotificationDistributorTest() {
	}

	@Test
	public void testRaiseNotification() throws PublicationException {
		final Account account = createNiceMock(Account.class);

		final NotificationDescriptor descriptor = new NotificationDescriptor("a", true);
		final Capture<PlayerStateListener> listenerCapture = new Capture<PlayerStateListener>();

		final NotificationPublisher internal = createStrictMock(NotificationPublisher.class);
		expect(internal.publishNotification(isA(Notification.class))).andReturn(true).times(8);
		replay(internal);

		final NotificationPublisher external = createStrictMock(NotificationPublisher.class);
		expect(external.publishNotification(isA(Notification.class))).andReturn(true); // published
		expect(external.publishNotification(isA(Notification.class))).andReturn(true); // published
		expect(external.publishNotification(isA(Notification.class))).andReturn(false); // rejected
		replay(external);

		final NotificationManager notificationManager = createMock(NotificationManager.class);
		expect(notificationManager.getDescriptor("a")).andReturn(descriptor).times(8);
		expect(notificationManager.isNotificationEnabled(account, "a")).andReturn(false); // 1: !enabled (ignore)
		expect(notificationManager.isNotificationEnabled(account, "a")).andReturn(true); // 2.1.1: !online && !redundant (publish)
		expect(notificationManager.isNotificationEnabled(account, "a")).andReturn(true); // 2.1.2.1: !online && redundant && !sent (publish)
		expect(notificationManager.getNotificationDate(account, "a")).andReturn(new Date(0));
		expect(notificationManager.isNotificationEnabled(account, "a")).andReturn(true); // 2.1.2.2: !online && redundant && sent (ignore)
		expect(notificationManager.getNotificationDate(account, "a")).andReturn(new Date(1));
		expect(notificationManager.isNotificationEnabled(account, "a")).andReturn(true); // 2.2.1.2: online && !mandatory && !redundant (postpone)
		expect(notificationManager.isNotificationEnabled(account, "a")).andReturn(true); // 2.2.1.2.1: online && !mandatory && redundant && !queue (postpone)
		expect(notificationManager.isNotificationEnabled(account, "a")).andReturn(true); // 2.2.1.2.2: online && !mandatory && redundant && queue (ignore)
		expect(notificationManager.isNotificationEnabled(account, "a")).andReturn(true); // 2.2.2: online && mandatory (publish)
		replay(notificationManager);

		final PlayerStateManager stateManager = createStrictMock(PlayerStateManager.class);
		stateManager.addPlayerStateListener(capture(listenerCapture));
		expect(stateManager.isPlayerOnline(account)).andReturn(false);
		expect(stateManager.isPlayerOnline(account)).andReturn(false);
		expect(stateManager.getLastActivityDate(account)).andReturn(new Date(1));
		expect(stateManager.isPlayerOnline(account)).andReturn(false);
		expect(stateManager.getLastActivityDate(account)).andReturn(new Date(0));
		expect(stateManager.isPlayerOnline(account)).andReturn(true);
		expect(stateManager.isPlayerOnline(account)).andReturn(true);
		expect(stateManager.isPlayerOnline(account)).andReturn(true);
		expect(stateManager.isPlayerOnline(account)).andReturn(true);
		replay(stateManager);

		final NotificationDistributorListener listener = createMock(NotificationDistributorListener.class);
		listener.notificationPublished(isA(Notification.class), eq(PublicationType.INTERNAL));
		expectLastCall().times(8);
		listener.notificationPublished(isA(Notification.class), eq(PublicationType.EXTERNAL));
		listener.notificationPublished(isA(Notification.class), eq(PublicationType.EXTERNAL));
		listener.notificationRejected(isA(Notification.class), eq(PublicationType.EXTERNAL));
		replay(listener);

		final DefaultNotificationDistributor distributor = new DefaultNotificationDistributor();
		distributor.setInternalPublisher(internal);
		distributor.setExternalPublisher(external);
		distributor.setTaskExecutor(new SyncTaskExecutor());
		distributor.setPlayerStateManager(stateManager);
		distributor.setNotificationManager(notificationManager);

		distributor.addNotificationDistributorListener(listener);

		distributor.raiseNotification("a", account, NotificationSender.GAME, "1: !enabled");

		distributor.setMandatoryNotifications(new HashSet<String>());
		distributor.setRedundantNotifications(new HashSet<String>());
		distributor.raiseNotification("a", account, NotificationSender.GAME, "2.1.1: !online && !redundant");

		distributor.setMandatoryNotifications(new HashSet<String>());
		distributor.setRedundantNotifications(new HashSet<String>(Arrays.asList("a")));
		distributor.raiseNotification("a", account, NotificationSender.GAME, "2.1.2.1: !online && redundant && !sent (publish)");

		distributor.setMandatoryNotifications(new HashSet<String>());
		distributor.setRedundantNotifications(new HashSet<String>(Arrays.asList("a")));
		distributor.raiseNotification("a", account, NotificationSender.GAME, "2.1.2.2: !online && redundant && sent (ignore)");

		distributor.setMandatoryNotifications(new HashSet<String>());
		distributor.setRedundantNotifications(new HashSet<String>());
		distributor.raiseNotification("a", account, NotificationSender.GAME, "2.2.1.2: online && !mandatory && !redundant (postpone)");

		distributor.setMandatoryNotifications(new HashSet<String>());
		distributor.setRedundantNotifications(new HashSet<String>(Arrays.asList("a")));
		distributor.raiseNotification("a", account, NotificationSender.GAME, "2.2.1.2.1: online && !mandatory && redundant && !queue (postpone)");

		distributor.setMandatoryNotifications(new HashSet<String>());
		distributor.setRedundantNotifications(new HashSet<String>(Arrays.asList("a")));
		distributor.raiseNotification("a", account, NotificationSender.GAME, "2.2.1.2.2: online && !mandatory && redundant && queue (ignore)");

		distributor.setMandatoryNotifications(new HashSet<String>(Arrays.asList("a")));
		distributor.setRedundantNotifications(new HashSet<String>());
		distributor.raiseNotification("a", account, NotificationSender.GAME, "2.2.2: online && mandatory (publish)");

		verify(internal, external, notificationManager, stateManager, listener);
	}
}
