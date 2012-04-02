package wisematches.server.web.services.notify.impl.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.impl.NotificationDescription;
import wisematches.server.web.services.notify.manager.NotificationCondition;
import wisematches.server.web.services.notify.manager.NotificationManager;

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

        NotificationCondition condition = notificationManager.getNotificationCondition(person);
        for (String name : condition.getNotificationNames()) {
            condition.setEnabled(name, true);
        }
        notificationManager.setNotificationCondition(person, condition);

        final Collection<NotificationDescription> descriptions = notificationManager.getDescriptions();
        for (NotificationDescription d : descriptions) {
            assertTrue(notificationManager.isNotificationEnabled(d, person));
        }

        condition = notificationManager.getNotificationCondition(person);
        System.out.println(condition);
        for (String name : condition.getNotificationNames()) {
            assertTrue(condition.isEnabled(name));
            condition.setEnabled(name, false);
        }
        notificationManager.setNotificationCondition(person, condition);

        for (NotificationDescription d : descriptions) {
            assertFalse(notificationManager.isNotificationEnabled(d, person));
        }
        condition = notificationManager.getNotificationCondition(person);
        for (String name : condition.getNotificationNames()) {
            assertFalse(condition.isEnabled(name));
        }
    }
}
