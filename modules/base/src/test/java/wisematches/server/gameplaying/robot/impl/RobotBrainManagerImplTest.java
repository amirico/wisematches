package wisematches.server.gameplaying.robot.impl;

import org.junit.Test;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotBrainManagerImplTest {

	@Test
	public void commented() {
		throw new UnsupportedOperationException("Test has been commented");
	}
/*
	@Test
	public void testGetBrains() {
		RobotBrainManagerImpl impl = new RobotBrainManagerImpl();

		final Room room1 = Room.valueOf("room1");
		final Room room2 = Room.valueOf("room2");

		final RobotBrain b1 = createBrain(room1, RobotType.DULL, RobotType.EXPERT);
		final RobotBrain b2 = createBrain(room1, RobotType.TRAINEE);
		final RobotBrain b3 = createBrain(room2, RobotType.TRAINEE);
		final RobotBrain b4 = createBrain(room2, RobotType.DULL, RobotType.TRAINEE);

		impl.setRobotBrains(Arrays.asList(b1, b2));
		assertEquals(3, impl.getRobotTypes(room1).size());
		assertSame(b1, impl.getRobotBrain(room1, RobotType.DULL));
		assertSame(b1, impl.getRobotBrain(room1, RobotType.EXPERT));
		assertSame(b2, impl.getRobotBrain(room1, RobotType.TRAINEE));


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
		assertSame(b3, impl.getRobotBrain(room2, RobotType.TRAINEE));
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
*/
}
