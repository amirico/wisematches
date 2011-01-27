package wisematches.server.gameplaying.room;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RoomsManager {
	private final Map<Room, RoomManager> roomManagers = new HashMap<Room, RoomManager>(0);

	public void setRoomManagers(Collection<RoomManager> roomManagers) {
		this.roomManagers.clear();

		if (roomManagers != null) {
			for (RoomManager roomManager : roomManagers) {
				this.roomManagers.put(roomManager.getRoomType(), roomManager);
			}
		}
	}

	public RoomManager getRoomManager(Room room) {
		return roomManagers.get(room);
	}

	public Collection<RoomManager> getRoomManagers() {
		return Collections.unmodifiableCollection(roomManagers.values());
	}
}
