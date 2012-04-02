package wisematches.server.web.services.notify.impl.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.hearer.NotificationManager;
import wisematches.server.web.services.notify.hearer.NotificationMask;

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
		"classpath:/config/notify-sender-config.xml",
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

		NotificationMask mask = notificationManager.getNotificationMask(person);
		for (String name : mask.getNotificationNames()) {
			mask.setEnabled(name, true);
		}
		notificationManager.setNotificationMask(person, mask);

		final Collection<NotificationDescription> descriptions = notificationManager.getDescriptions();
		for (NotificationDescription d : descriptions) {
			assertTrue(notificationManager.isNotificationEnabled(d, person));
		}

		mask = notificationManager.getNotificationMask(person);
		System.out.println(mask);
		for (String name : mask.getNotificationNames()) {
			assertTrue(mask.isEnabled(name));
			mask.setEnabled(name, false);
		}
		notificationManager.setNotificationMask(person, mask);

		for (NotificationDescription d : descriptions) {
			assertFalse(notificationManager.isNotificationEnabled(d, person));
		}
		mask = notificationManager.getNotificationMask(person);
		for (String name : mask.getNotificationNames()) {
			assertFalse(mask.isEnabled(name));
		}
	}
}
