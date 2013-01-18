package wisematches.server.web.services.state.impl;

import org.junit.Test;
import wisematches.core.personality.Player;
import wisematches.core.personality.proprietary.guest.GuestPlayer;
import wisematches.core.personality.proprietary.robot.RobotPlayer;
import wisematches.server.security.impl.WMPlayerDetails;
import wisematches.server.web.services.state.PlayerStateListener;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SessionRegistryStateManagerTest {
	public SessionRegistryStateManagerTest() {
	}

	@Test
	public void test() {
		final Player player1 = GuestPlayer.GUEST;
		final Player player2 = RobotPlayer.DULL;

		final PlayerStateListener listener = createStrictMock(PlayerStateListener.class);

		final SessionRegistryStateManager stateManager = new SessionRegistryStateManager();
		stateManager.addPlayerStateListener(listener);

		listener.playerOnline(player1);
		listener.playerOnline(player2);
		listener.playerAlive(player1);
		listener.playerAlive(player1);
		listener.playerOffline(player2);
		listener.playerOffline(player1);
		replay(listener);

		assertFalse(stateManager.isPlayerOnline(player1));
		stateManager.registerNewSession("S1", new WMPlayerDetails(player1, "asd", "qwe", false));
		assertTrue(stateManager.isPlayerOnline(player1));
		stateManager.registerNewSession("S2", new WMPlayerDetails(player1, "asd", "qwe", false));
		assertFalse(stateManager.isPlayerOnline(player2));
		stateManager.registerNewSession("S3", new WMPlayerDetails(player2, "asd", "qwe", false));
		assertTrue(stateManager.isPlayerOnline(player2));
		stateManager.registerNewSession("S4", new WMPlayerDetails(player2, "asd", "qwe", false));
		stateManager.refreshLastRequest("S5");
		stateManager.refreshLastRequest("S1");
		stateManager.refreshLastRequest("S1");
		stateManager.removeSessionInformation("S5");
		assertTrue(stateManager.isPlayerOnline(player1));
		assertTrue(stateManager.isPlayerOnline(player2));
		stateManager.removeSessionInformation("S2");
		assertTrue(stateManager.isPlayerOnline(player1));
		stateManager.removeSessionInformation("S3");
		assertTrue(stateManager.isPlayerOnline(player2));
		stateManager.removeSessionInformation("S4");
		assertFalse(stateManager.isPlayerOnline(player2));
		stateManager.removeSessionInformation("S1");
		assertFalse(stateManager.isPlayerOnline(player1));

		verify(listener);
	}
}
