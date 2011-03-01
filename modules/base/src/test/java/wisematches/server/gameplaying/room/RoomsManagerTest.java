package wisematches.server.gameplaying.room;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@SuppressWarnings("unchecked")
public class RoomsManagerTest {
	public RoomsManagerTest() {
	}

	@Test
	public void testRoomsManager() {
		RoomsManager rm = new RoomsManager();
		assertEquals(0, rm.getRoomManagers().size());
		assertNull(rm.getRoomManager(MockRoom.type));

		List<RoomManager> c = new ArrayList<RoomManager>(0);
		c.add(createMockRoom(MockRoom.type2, 1));
		c.add(createMockRoom(MockRoom.type3, 1));

		rm.setRoomManagers(c);
		assertEquals(2, rm.getRoomManagers().size());

		c.add(createStrictMock(RoomManager.class));
		assertEquals(2, rm.getRoomManagers().size());

		assertSame(c.get(0), rm.getRoomManager(MockRoom.type2));
	}

	private RoomManager createMockRoom(Room room, int times) {
		final RoomManager mock = createMock(RoomManager.class);
		expect(mock.getRoomType()).andReturn(room).times(times);
		replay(mock);
		return mock;
	}
}