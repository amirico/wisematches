package wisematches.server.games.room;

import java.util.HashMap;
import java.util.Map;

/**
 * A <code>RoomId</code> class represents a id of one room.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Room {
	private final String name;

	private static final Map<String, Room> rooms = new HashMap<String, Room>(0);

	/**
	 * Creates new room id with specified name.
	 *
	 * @param name the room name.
	 */
	private Room(String name) {
		this.name = name;
	}

	/**
	 * Returns name of this room.
	 *
	 * @return the name of the room.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return room by it's name. This method always returns a <code>Room</code> with specified name. If one
	 * doesn't exist this method creates new.
	 *
	 * @param name the name of room.
	 * @return the room by specified name.
	 */
	public static Room valueOf(String name) {
		Room room = rooms.get(name);
		if (room == null) {
			room = new Room(name);
			rooms.put(name, room);
		}
		return room;
	}

	/**
	 * Returns name of this room.
	 *
	 * @return the name of this room.
	 */
	public String toString() {
		return name;
	}
}