package wisematches.server.web.services.notice.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notice.NotificationDescription;
import wisematches.server.web.services.notice.NotificationManager;
import wisematches.server.web.services.notice.NotificationMask;

import java.util.Collection;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

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
		"classpath:/config/application-settings.xml",
		"classpath:/config/server-web-junit-config.xml"
})
public class HibernateNotificationManagerTest {
	@Autowired
	NotificationManager notificationManager;

	public HibernateNotificationManagerTest() {
	}

	@Test
	public void test() {
		final Personality person = Personality.person(1);

		NotificationMask notificationMask = notificationManager.getNotificationMask(person);
		for (String name : notificationMask.getNotificationNames()) {
			notificationMask.setEnabled(name, true);
		}
		notificationManager.setNotificationMask(person, notificationMask);

		final Collection<NotificationDescription> descriptions = notificationManager.getDescriptions();
		for (NotificationDescription d : descriptions) {
			assertTrue(notificationManager.isNotificationEnabled(d.getName(), person));
		}

		notificationMask = notificationManager.getNotificationMask(person);
		System.out.println(notificationMask);
		for (String name : notificationMask.getNotificationNames()) {
			assertTrue(notificationMask.isEnabled(name));
			notificationMask.setEnabled(name, false);
		}
		notificationManager.setNotificationMask(person, notificationMask);

		for (NotificationDescription d : descriptions) {
			assertFalse(notificationManager.isNotificationEnabled(d.getName(), person));
		}
		notificationMask = notificationManager.getNotificationMask(person);
		for (String name : notificationMask.getNotificationNames()) {
			assertFalse(notificationMask.isEnabled(name));
		}
	}
}
