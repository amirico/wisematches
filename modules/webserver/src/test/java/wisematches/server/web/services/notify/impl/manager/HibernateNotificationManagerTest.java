package wisematches.server.web.services.notify.impl.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.NotificationSettings;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationManager;

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
        "classpath:/config/notifications-config.xml",
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

        NotificationSettings settings = notificationManager.getNotificationCondition(person);
        for (String name : settings.getNotificationNames()) {
            settings.setEnabled(name, true);
        }
        notificationManager.setNotificationCondition(person, settings);

        final Collection<NotificationDescriptor> descriptions = notificationManager.getDescriptors();
        for (NotificationDescriptor d : descriptions) {
            assertTrue(notificationManager.isNotificationEnabled(person, d.getCode()));
        }

        settings = notificationManager.getNotificationCondition(person);
        System.out.println(settings);
        for (String name : settings.getNotificationNames()) {
            assertTrue(settings.isEnabled(name));
            settings.setEnabled(name, false);
        }
        notificationManager.setNotificationCondition(person, settings);

        for (NotificationDescriptor d : descriptions) {
            assertFalse(notificationManager.isNotificationEnabled(person, d.getCode()));
        }
        settings = notificationManager.getNotificationCondition(person);
        for (String name : settings.getNotificationNames()) {
            assertFalse(settings.isEnabled(name));
        }
    }
}
