package wisematches.server.services.state.impl;

import org.junit.Test;
import wisematches.core.Language;
import wisematches.core.Player;
import wisematches.core.personality.DefaultVisitor;
import wisematches.core.secure.PlayerContainer;
import wisematches.server.services.state.PlayerStateListener;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SessionRegistryStateManagerTest {
	public SessionRegistryStateManagerTest() {
	}

	@Test
	public void test() {
		final Player player1 = new DefaultVisitor(Language.RU);
		final Player player2 = new DefaultVisitor(Language.EN);

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
		stateManager.registerNewSession("S1", createContainer(player1));
		assertTrue(stateManager.isPlayerOnline(player1));
		stateManager.registerNewSession("S2", createContainer(player1));
		assertFalse(stateManager.isPlayerOnline(player2));
		stateManager.registerNewSession("S3", createContainer(player2));
		assertTrue(stateManager.isPlayerOnline(player2));
		stateManager.registerNewSession("S4", createContainer(player2));
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

	private PlayerContainer createContainer(Player player) {
		PlayerContainer container = createMock(PlayerContainer.class);
		expect(container.getPlayer()).andReturn(player).anyTimes();
		replay(container);
		return container;
	}
}
