package wisematches.server.services.state.impl;

import org.junit.Test;
import wisematches.core.Personality;
import wisematches.core.RobotType;
import wisematches.core.personality.DefaultRobot;
import wisematches.core.security.userdetails.PersonalityDetails;
import wisematches.server.services.state.PlayerStateListener;

import java.util.Arrays;

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
		final Personality player1 = new DefaultRobot(RobotType.DULL);
		final Personality player2 = new DefaultRobot(RobotType.EXPERT);

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
		stateManager.registerNewSession("S1", new PersonalityDetails(player1, "asd", "qwe", false, false, Arrays.asList("mock")));
		assertTrue(stateManager.isPlayerOnline(player1));
		stateManager.registerNewSession("S2", new PersonalityDetails(player1, "asd", "qwe", false, false, Arrays.asList("mock")));
		assertFalse(stateManager.isPlayerOnline(player2));
		stateManager.registerNewSession("S3", new PersonalityDetails(player2, "asd", "qwe", false, false, Arrays.asList("mock")));
		assertTrue(stateManager.isPlayerOnline(player2));
		stateManager.registerNewSession("S4", new PersonalityDetails(player2, "asd", "qwe", false, false, Arrays.asList("mock")));
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
