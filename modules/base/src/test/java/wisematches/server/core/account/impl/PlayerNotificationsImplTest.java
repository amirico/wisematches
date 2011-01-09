package wisematches.server.core.account.impl;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerNotificationsImplTest {
    private final PlayerNotificationsImpl notifications = new PlayerNotificationsImpl();

    @Test
    public void test_common() {
        assertEquals("", notifications.getNotifications());

        assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
        assertTrue(notifications.isNotificationEnabled(MockPlayerNotification2.V1));

        notifications.addDisabledNotification(MockPlayerNotification1.V1);
        assertFalse(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
        assertTrue(notifications.isNotificationEnabled(MockPlayerNotification2.V1));
        assertEquals("TYPE1-V1", notifications.getNotifications());

        notifications.addDisabledNotification(MockPlayerNotification2.V1);
        assertFalse(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
        assertFalse(notifications.isNotificationEnabled(MockPlayerNotification2.V1));
        assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V2));
        assertTrue(notifications.isNotificationEnabled(MockPlayerNotification2.V2));
        assertEquals("TYPE1-V1|TYPE2-V1", notifications.getNotifications());

        notifications.removeDisabledNotification(MockPlayerNotification1.V1);
        assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V1));
        assertFalse(notifications.isNotificationEnabled(MockPlayerNotification2.V1));
        assertTrue(notifications.isNotificationEnabled(MockPlayerNotification1.V2));
        assertTrue(notifications.isNotificationEnabled(MockPlayerNotification2.V2));
        assertEquals("TYPE2-V1", notifications.getNotifications());
    }
}
