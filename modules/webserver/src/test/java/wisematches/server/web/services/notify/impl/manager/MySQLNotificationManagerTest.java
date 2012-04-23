package wisematches.server.web.services.notify.impl.manager;

import org.easymock.Capture;
import org.easymock.IMockBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.*;
import wisematches.server.web.services.notify.impl.distributor.DefaultNotificationDistributor;

import java.util.Collection;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-junit-config.xml",
		"classpath:/config/notifications-config.xml",
		"classpath:/config/application-settings.xml",
		"classpath:/config/server-web-junit-config.xml"
})
public class MySQLNotificationManagerTest {
	@Autowired
	private NotificationManager notificationManager;

	@Autowired
	private DefaultNotificationDistributor notificationDistributor;

	public MySQLNotificationManagerTest() {
	}

	@Test
	public void testNotificationSettings() {
		final Personality person = Personality.person(1);

		final Capture<NotificationSettings> oldSettings = new Capture<NotificationSettings>();
		final Capture<NotificationSettings> newSettings = new Capture<NotificationSettings>();

		final NotificationManagerListener managerListener = createStrictMock(NotificationManagerListener.class);
		managerListener.notificationConditionChanged(eq(person), capture(oldSettings), capture(newSettings));
		managerListener.notificationConditionChanged(eq(person), capture(oldSettings), capture(newSettings));
		replay(managerListener);

		notificationManager.addNotificationManagerListener(managerListener);

		NotificationSettings settings = notificationManager.getNotificationSettings(person);
		for (String name : settings.getNotificationNames()) {
			settings.setEnabled(name, true);
		}
		notificationManager.setNotificationSettings(person, settings);

		final Collection<NotificationDescriptor> descriptions = notificationManager.getDescriptors();
		for (NotificationDescriptor d : descriptions) {
			assertTrue(notificationManager.isNotificationEnabled(person, d.getCode()));
		}

		settings = notificationManager.getNotificationSettings(person);
		System.out.println(settings);
		for (String name : settings.getNotificationNames()) {
			assertTrue(settings.isEnabled(name));
			settings.setEnabled(name, false);
		}
		notificationManager.setNotificationSettings(person, settings);

		for (NotificationDescriptor d : descriptions) {
			assertFalse(notificationManager.isNotificationEnabled(person, d.getCode()));
		}
		settings = notificationManager.getNotificationSettings(person);
		for (String name : settings.getNotificationNames()) {
			assertFalse(settings.isEnabled(name));
		}

		assertFalse(oldSettings.getValue() == newSettings.getValue());

		notificationManager.removeNotificationManagerListener(managerListener);

		verify(managerListener);
	}

	@Test
	public void testLastNotificationDate() throws PublicationException {
		IMockBuilder<Account> mockBuilder = createMockBuilder(Account.class);
		mockBuilder.withConstructor(1L);

		final Account mock = mockBuilder.createMock("mock1");

		final NotificationPublisher internalPublisher = createStrictMock(NotificationPublisher.class);
		expect(internalPublisher.publishNotification(isA(Notification.class))).andReturn(true);
		replay(internalPublisher);

		final NotificationPublisher externalPublisher = createStrictMock(NotificationPublisher.class);
		expect(externalPublisher.publishNotification(isA(Notification.class))).andReturn(true);
		replay(externalPublisher);

		notificationDistributor.setInternalPublisher(internalPublisher);
		notificationDistributor.setExternalPublisher(externalPublisher);
		notificationDistributor.setTaskExecutor(new SyncTaskExecutor());

		final NotificationSettings settings = notificationManager.getNotificationSettings(mock);
		// clear last notification date
		settings.setEnabled("game.state.started", false);
		notificationManager.setNotificationSettings(mock, settings);
		settings.setEnabled("game.state.started", true);
		notificationManager.setNotificationSettings(mock, settings);

		assertNull(notificationManager.getNotificationDate(mock, "game.state.started"));
		notificationDistributor.raiseNotification("game.state.started", mock, NotificationSender.GAME, null);

		assertNotNull(notificationManager.getNotificationDate(mock, "game.state.started"));

		settings.setEnabled("game.state.started", false);
		notificationManager.setNotificationSettings(mock, settings);
		assertNull(notificationManager.getNotificationDate(mock, "game.state.started"));

		settings.setEnabled("game.state.started", true);
		notificationManager.setNotificationSettings(mock, settings);
		assertNull(notificationManager.getNotificationDate(mock, "game.state.started"));

		verify(internalPublisher);
		verify(externalPublisher);
	}
}
