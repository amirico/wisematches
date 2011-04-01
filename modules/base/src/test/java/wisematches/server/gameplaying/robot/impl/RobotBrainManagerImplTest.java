package wisematches.server.gameplaying.robot.impl;

import org.junit.Test;
import wisematches.server.gameplaying.robot.RobotBrain;
import wisematches.server.gameplaying.room.MockRoom;
import wisematches.server.gameplaying.room.Room;
import wisematches.server.personality.player.computer.robot.RobotType;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotBrainManagerImplTest {
	public RobotBrainManagerImplTest() {
	}

	@Test
	public void testGetBrains() {
		RobotBrainManagerImpl impl = new RobotBrainManagerImpl();

		final RobotBrain b1 = createBrain(MockRoom.type2, RobotType.DULL, RobotType.EXPERT);
		final RobotBrain b2 = createBrain(MockRoom.type2, RobotType.TRAINEE);
		final RobotBrain b3 = createBrain(MockRoom.type3, RobotType.TRAINEE);
		final RobotBrain b4 = createBrain(MockRoom.type3, RobotType.DULL, RobotType.TRAINEE);

		impl.setRobotBrains(Arrays.asList(b1, b2));
		assertEquals(3, impl.getRobotTypes(MockRoom.type2).size());
		assertSame(b1, impl.getRobotBrain(MockRoom.type2, RobotType.DULL));
		assertSame(b1, impl.getRobotBrain(MockRoom.type2, RobotType.EXPERT));
		assertSame(b2, impl.getRobotBrain(MockRoom.type2, RobotType.TRAINEE));


		impl.addRobotBrain(b3);
		assertEquals(3, impl.getRobotTypes(MockRoom.type2).size());
		assertEquals(1, impl.getRobotTypes(MockRoom.type3).size());

		try {
			impl.addRobotBrain(b4);
			fail("Exception must be here: robot for specified room and with specified type already exist.");
		} catch (IllegalArgumentException ex) {
			;
		}

		assertEquals(1, impl.getRobotTypes(MockRoom.type3).size());
		assertSame(b3, impl.getRobotBrain(MockRoom.type3, RobotType.TRAINEE));
		assertNull(impl.getRobotBrain(MockRoom.type3, RobotType.DULL));

		impl.removeRobotBrain(b1);
		assertEquals(1, impl.getRobotTypes(MockRoom.type2).size());
		assertEquals(1, impl.getRobotTypes(MockRoom.type3).size());
	}

	private RobotBrain createBrain(Room room, RobotType... types) {
		RobotBrain brain = createMock(RobotBrain.class);
		expect(brain.getRoom()).andReturn(room).anyTimes();
		expect(brain.getRobotTypes()).andReturn(types).anyTimes();
		replay(brain);
		return brain;
	}
}
