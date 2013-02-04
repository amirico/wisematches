package wisematches.server.web.services.state.impl;

import org.junit.Test;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SessionRegistryStateManagerTest {
	public SessionRegistryStateManagerTest() {
	}

	@Test
	public void test() {
		throw new UnsupportedOperationException("Commented");
/*
		final Personality player1 = GuestPlayer.GUEST;
		final Personality player2 = RobotPlayer.DULL;

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
*/
	}
}
