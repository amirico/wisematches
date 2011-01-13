package wisematches.server.robot.impl;

import org.junit.Test;
import wisematches.server.games.room.Room;
import wisematches.server.player.Player;
import wisematches.server.robot.RobotBrain;
import wisematches.server.robot.RobotType;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotsBrainManagerImplTest {
	@Test
	public void testDefaulRobotPlayers() {
		RobotsBrainManagerImpl impl = new RobotsBrainManagerImpl();
		assertEquals(3, impl.getRobotPlayers().size());

		assertEquals(1L, impl.getPlayer(1L).getId());
		assertEquals(2L, impl.getPlayer(2L).getId());
		assertEquals(3L, impl.getPlayer(3L).getId());
		assertNull(impl.getPlayer(4L));

		assertTrue(impl.isRobotPlayer(1L));
		assertFalse(impl.isRobotPlayer(4L));

		assertTrue(impl.isRobotPlayer(impl.getPlayer(1L)));
		assertFalse(impl.isRobotPlayer(createNiceMock(Player.class)));
	}

	@Test
	public void testGetBrains() {
		RobotsBrainManagerImpl impl = new RobotsBrainManagerImpl();

		final Room room1 = Room.valueOf("room1");
		final Room room2 = Room.valueOf("room2");

		final RobotBrain b1 = createBrain(room1, RobotType.DULL, RobotType.EXPERT);
		final RobotBrain b2 = createBrain(room1, RobotType.STAGER);
		final RobotBrain b3 = createBrain(room2, RobotType.STAGER);
		final RobotBrain b4 = createBrain(room2, RobotType.DULL, RobotType.STAGER);

		impl.setRobotBrains(Arrays.asList(b1, b2));
		assertEquals(3, impl.getRobotTypes(room1).size());
		assertSame(b1, impl.getRobotBrain(room1, RobotType.DULL));
		assertSame(b1, impl.getRobotBrain(room1, RobotType.EXPERT));
		assertSame(b2, impl.getRobotBrain(room1, RobotType.STAGER));


		impl.addRobotBrain(b3);
		assertEquals(3, impl.getRobotTypes(room1).size());
		assertEquals(1, impl.getRobotTypes(room2).size());

		try {
			impl.addRobotBrain(b4);
			fail("Exception must be here: robot for specified room and with specified type already exist.");
		} catch (IllegalArgumentException ex) {
			;
		}

		assertEquals(1, impl.getRobotTypes(room2).size());
		assertSame(b3, impl.getRobotBrain(room2, RobotType.STAGER));
		assertNull(impl.getRobotBrain(room2, RobotType.DULL));

		impl.removeRobotBrain(b1);
		assertEquals(1, impl.getRobotTypes(room1).size());
		assertEquals(1, impl.getRobotTypes(room2).size());
	}

	private RobotBrain createBrain(Room room, RobotType... types) {
		RobotBrain brain = createMock(RobotBrain.class);
		expect(brain.getRoom()).andReturn(room).anyTimes();
		expect(brain.getRobotTypes()).andReturn(types).anyTimes();
		replay(brain);
		return brain;
	}
}
