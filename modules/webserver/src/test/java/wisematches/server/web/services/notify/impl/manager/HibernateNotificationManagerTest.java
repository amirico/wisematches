package wisematches.server.web.services.notify.impl.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.NotificationCondition;
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

        NotificationCondition condition = notificationManager.getNotificationCondition(person);
        for (String name : condition.getNotificationNames()) {
            condition.setEnabled(name, true);
        }
        notificationManager.setNotificationCondition(person, condition);

        final Collection<NotificationDescriptor> descriptions = notificationManager.getDescriptors();
        for (NotificationDescriptor d : descriptions) {
            assertTrue(notificationManager.isNotificationEnabled(person, d.getCode()));
        }

        condition = notificationManager.getNotificationCondition(person);
        System.out.println(condition);
        for (String name : condition.getNotificationNames()) {
            assertTrue(condition.isEnabled(name));
            condition.setEnabled(name, false);
        }
        notificationManager.setNotificationCondition(person, condition);

        for (NotificationDescriptor d : descriptions) {
            assertFalse(notificationManager.isNotificationEnabled(person, d.getCode()));
        }
        condition = notificationManager.getNotificationCondition(person);
        for (String name : condition.getNotificationNames()) {
            assertFalse(condition.isEnabled(name));
        }
    }
}
