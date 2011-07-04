package wisematches.server.web.services.notice.publisher;

import org.junit.Before;
import org.junit.Test;
import wisematches.personality.Personality;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.server.web.services.notice.NotificationDescription;
import wisematches.server.web.services.notice.NotificationManager;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.Arrays;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationPublishCenterTest {
	private NotificationPublisher publisher;

	private PlayerStateManager playerStateManager;
	private NotificationManager notificationManager;
	private NotificationPublishCenter publishCenter;

	private NotificationDescription d1 = new NotificationDescription("d1", "", null, true, true);
	private NotificationDescription d2 = new NotificationDescription("d2", "", null, false, true);
	private NotificationDescription d3 = new NotificationDescription("d3", "", null, true, false);
	private NotificationDescription d4 = new NotificationDescription("d4", "", "s1", true, true);
	private NotificationDescription d5 = new NotificationDescription("d5", "", "s1", true, true);

	public NotificationPublishCenterTest() {
	}

	@Before
	public void setUp() {
		publisher = createStrictMock(NotificationPublisher.class);
		playerStateManager = createStrictMock(PlayerStateManager.class);
		notificationManager = createStrictMock(NotificationManager.class);

		playerStateManager.addPlayerStateListener(isA(PlayerStateListener.class));
		replay(playerStateManager);

		publishCenter = new NotificationPublishCenter();
		publishCenter.setPublishers(Arrays.asList(publisher));
		publishCenter.setNotificationManager(notificationManager);
		publishCenter.setPlayerStateManager(playerStateManager);

		reset(playerStateManager);
	}

	@Test
	public void testProcessNotification() {
		final Personality person = Personality.person(999);

		final Notification n1 = new Notification(RobotPlayer.DULL, d1, this);
		final Notification n2 = new Notification(person, d1, this);
		final Notification n3 = new Notification(person, d2, this);
		final Notification n4 = new Notification(person, d3, this);
		final Notification n5 = new Notification(person, d4, this);
		final Notification n6 = new Notification(person, d5, this);

		publisher.publishNotification(n2);
		publisher.publishNotification(n3);
		publisher.publishNotification(n4);
		publisher.publishNotification(n6);
		replay(publisher);

		expect(notificationManager.isNotificationEnabled("d1", person)).andReturn(true);
		expect(playerStateManager.isPlayerOnline(person)).andReturn(false);
		expect(notificationManager.isNotificationEnabled("d2", person)).andReturn(true);
		expect(playerStateManager.isPlayerOnline(person)).andReturn(true);
		expect(notificationManager.isNotificationEnabled("d3", person)).andReturn(true);
		expect(playerStateManager.isPlayerOnline(person)).andReturn(true);
		expect(notificationManager.isNotificationEnabled("d5", person)).andReturn(true);
		expect(playerStateManager.isPlayerOnline(person)).andReturn(true);

		replay(notificationManager, playerStateManager);

		publishCenter.processNotification(n1); // nothing - player is robot
		publishCenter.processNotification(n2);
		publishCenter.processNotification(n3);
		publishCenter.processNotification(n4);
		publishCenter.processNotifications(Arrays.asList(n5, n6));

		verify(publisher, notificationManager);
	}
}
