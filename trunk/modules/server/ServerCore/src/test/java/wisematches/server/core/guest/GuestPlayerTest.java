package wisematches.server.core.guest;

import static org.easymock.EasyMock.createNiceMock;
import static org.junit.Assert.*;
import org.junit.Test;
import wisematches.kernel.notification.PlayerNotifications;
import wisematches.kernel.notification.PlayerNotification;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GuestPlayerTest {
    @Test
    public void test_getNationalityPlayer() {
        final GuestPlayer guestPlayer = GuestPlayer.GUEST_PLAYER;
        assertEquals(GuestPlayer.PLAYER_ID, guestPlayer.getId());
    }

    @Test
    public void test_notification() {
        final GuestPlayer guestPlayer = GuestPlayer.GUEST_PLAYER;
        assertNotNull(guestPlayer.getPlayerNotifications());

        final PlayerNotification niceMock = createNiceMock(PlayerNotification.class);
        final PlayerNotifications playerNotifications = guestPlayer.getPlayerNotifications();
        assertFalse(playerNotifications.isNotificationEnabled(niceMock));

        playerNotifications.addDisabledNotification(niceMock);
        assertFalse(playerNotifications.isNotificationEnabled(niceMock));

        playerNotifications.removeDisabledNotification(niceMock);
        assertFalse(playerNotifications.isNotificationEnabled(niceMock));
    }
}
