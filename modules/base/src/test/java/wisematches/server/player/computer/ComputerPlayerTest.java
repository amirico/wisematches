package wisematches.server.player.computer;

import org.junit.Test;
import wisematches.server.player.Membership;
import wisematches.server.player.computer.guest.GuestPlayer;
import wisematches.server.player.computer.robot.RobotPlayer;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ComputerPlayerTest {
	@Test
	public void testNotUniquePlayers() {
		new ComputerPlayer(0, "mock", Membership.ROBOT);
		try {
			new ComputerPlayer(0, "mock", Membership.ROBOT);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	public void testGetComputerPlayer() {
		ComputerPlayer p1 = new ComputerPlayer(999, "mock", Membership.ROBOT);
		ComputerPlayer p2 = new ComputerPlayer(998, "mock", Membership.ROBOT);

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
