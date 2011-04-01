package wisematches.server.gameplaying.robot.impl;

import wisematches.server.gameplaying.robot.RobotBrain;
import wisematches.server.gameplaying.robot.RobotBrainManager;
import wisematches.server.gameplaying.room.Room;
import wisematches.server.personality.player.computer.robot.RobotPlayer;
import wisematches.server.personality.player.computer.robot.RobotType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of {@code RobotsBrainManager}. This implementation contains methods
 * to add brains and creates three predefined {@code RobotPlayer}s: one for each type.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotBrainManagerImpl implements RobotBrainManager {
	private final Map<Room, Map<RobotType, RobotBrain>> robotBrains = new HashMap<Room, Map<RobotType, RobotBrain>>();

	public RobotBrainManagerImpl() {
	}

	public void addRobotBrain(RobotBrain brain) {
		final Room room = brain.getRoom();
		final Map<RobotType, RobotBrain> map = getBrainsMap(room, true);

		final RobotType[] types = brain.getRobotTypes();
		for (RobotType type : types) {
			if (map.containsKey(type)) {
				throw new IllegalArgumentException("Brain can be added: manager contains brain of type " + type +
						" for room " + room);
			}
		}

		for (RobotType type : types) {
			map.put(type, brain);
		}
	}

	public void removeRobotBrain(RobotBrain brain) {
		final Room room = brain.getRoom();
		final Map<RobotType, RobotBrain> map = getBrainsMap(room, false);

		final RobotType[] types = brain.getRobotTypes();
		for (RobotType type : types) {
			map.remove(type);
		}
	}

	public void setRobotBrains(Collection<RobotBrain> robotBrains) {
		this.robotBrains.clear();

		for (RobotBrain robotBrain : robotBrains) {
			addRobotBrain(robotBrain);
		}
	}

	public Collection<RobotType> getRobotTypes(Room room) {
		return Collections.unmodifiableCollection(getBrainsMap(room, false).keySet());
	}

	public RobotBrain getRobotBrain(Room room, RobotType robotType) {
		return getBrainsMap(room, false).get(robotType);
	}

	public Collection<RobotPlayer> getRobotPlayers() {
		return RobotPlayer.getRobotPlayers();
	}

	private Map<RobotType, RobotBrain> getBrainsMap(Room room, boolean createNotExist) {
		Map<RobotType, RobotBrain> map = robotBrains.get(room);
		if (map == null) {
			if (createNotExist) {
				map = new HashMap<RobotType, RobotBrain>();
				robotBrains.put(room, map);
			} else {
				map = Collections.emptyMap();
			}
		}
		return map;
	}
}
