package wisematches.playground.robot;

import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.computer.robot.RobotType;
import wisematches.playground.room.Room;

import java.util.Collection;

/**
 * Robots brain manager is a manager class that contains brains for all games that supports robots.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RobotBrainManager {
	/**
	 * Returns unmodifiable collection or robot type for specified room. Room can contains zero or
	 * more types and number of this types are depends on room.
	 *
	 * @param room the room that supported robot types should be returned.
	 * @return unmodifiable collection of supported robot type for specified room. If room does not support
	 *         any types empty collection will be returned.
	 */
	Collection<RobotType> getRobotTypes(Room room);

	/**
	 * Returns collection of all supported robot players.
	 *
	 * @return the unmodifiable collection of robot players.
	 */
	Collection<RobotPlayer> getRobotPlayers();

	/**
	 * Returns brain of specified type for specified room.
	 *
	 * @param room	  the room id.
	 * @param robotType the type of brain that should be returned.
	 * @return the brain of specified type for specified room.
	 * @throws IllegalArgumentException if room is unknown or room doesn't have brain of specified type.
	 */
	RobotBrain getRobotBrain(Room room, RobotType robotType);
}