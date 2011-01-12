package wisematches.server.robot.impl;

import org.easymock.IAnswer;
import org.junit.Test;
import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerListener;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.guest.GuestPlayerManager;
import wisematches.server.robot.RobotsBrainManager;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class CompositePlayerManagerTest {
	private PlayerListener playerListener;

	@Test
	public void test_PlayerListener() {
		final Player player1 = createNiceMock(Player.class);
		final Player player2 = createNiceMock(Player.class);
		final Player player3 = createNiceMock(Player.class);

		final PlayerListener listener = createStrictMock(PlayerListener.class);
		listener.playerUpdated(player1);
		listener.playerUpdated(player2);
		listener.playerUpdated(player3);
		replay(listener);

		final CompositePlayerManager cpm = new CompositePlayerManager();
		cpm.addPlayerListener(listener);
		cpm.addPlayerListener(listener); //Twise adding - nothing should be happend

		final GuestPlayerManager gpm = createMock(GuestPlayerManager.class);
		expectPlayerListener(gpm);
		replay(gpm);

		final RobotsBrainManager rpm = createMock(RobotsBrainManager.class);
		expectPlayerListener(rpm);
		replay(rpm);

		final PlayerManager pm = createMock(PlayerManager.class);
		expectPlayerListener(pm);
		replay(pm);

		cpm.setGuestPlayerManager(gpm);
		playerListener.playerUpdated(player1);

		cpm.setRobotsBrainManager(rpm);
		playerListener.playerUpdated(player2);

		cpm.setRealPlayerManager(pm);
		playerListener.playerUpdated(player3);

		verify(listener, gpm, rpm, pm);
	}

	private void expectPlayerListener(PlayerManager playerManager) {
		playerManager.addPlayerListener(isA(PlayerListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				playerListener = (PlayerListener) getCurrentArguments()[0];
				return null;
			}
		});
	}
}
