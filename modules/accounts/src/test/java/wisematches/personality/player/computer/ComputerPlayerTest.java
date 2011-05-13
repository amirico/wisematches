package wisematches.personality.player.computer;

import org.junit.Test;
import wisematches.personality.player.computer.guest.GuestPlayer;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.server.personality.account.Membership;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ComputerPlayerTest {
	@Test
	public void testNotUniquePlayers() {
		new ComputerPlayer(0, "mock", Membership.ROBOT, (short) 100);
		try {
			new ComputerPlayer(0, "mock", Membership.ROBOT, (short) 100);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	public void testGetComputerPlayer() {
		ComputerPlayer p1 = new ComputerPlayer(999, "mock", Membership.ROBOT, (short) 100);
		ComputerPlayer p2 = new ComputerPlayer(998, "mock", Membership.ROBOT, (short) 100);

		assertSame(p1, ComputerPlayer.<ComputerPlayer>getComputerPlayer(999));
		assertSame(p2, ComputerPlayer.<ComputerPlayer>getComputerPlayer(998));
		assertNull(ComputerPlayer.<ComputerPlayer>getComputerPlayer(997));
	}

	@Test
	public void testGetComputerPlayers() {
		assertEquals(RobotPlayer.getRobotPlayers(), ComputerPlayer.getComputerPlayers(RobotPlayer.class));
		assertEquals(Arrays.asList(GuestPlayer.GUEST), ComputerPlayer.getComputerPlayers(GuestPlayer.class));
	}
}
