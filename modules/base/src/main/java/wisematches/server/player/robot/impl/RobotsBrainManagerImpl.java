package wisematches.server.player.robot.impl;

import wisematches.server.gameplaying.room.Room;
import wisematches.server.player.Player;
import wisematches.server.player.PlayerListener;
import wisematches.server.player.robot.RobotBrain;
import wisematches.server.player.robot.RobotPlayer;
import wisematches.server.player.robot.RobotType;
import wisematches.server.player.robot.RobotsBrainManager;

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
public class RobotsBrainManagerImpl implements RobotsBrainManager {
	private final Map<Long, RobotPlayer> robotPlayerMap = new HashMap<Long, RobotPlayer>();
	private final Map<Room, Map<RobotType, RobotBrain>> robotBrains = new HashMap<Room, Map<RobotType, RobotBrain>>();

	public RobotsBrainManagerImpl() {
		long index = 1;
		final RobotType[] types = RobotType.values();
		for (RobotType type : types) {
			robotPlayerMap.put(index, new RobotPlayerImpl(index, type));
			index++;
		}
	}

	public void addPlayerListener(PlayerListener l) {
	}

	public void removePlayerListener(PlayerListener l) {
	}

	@Override
	public Player findByEmail(String email) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Player findByUsername(String username) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
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

	public RobotPlayer getPlayer(long playerId) {
		return robotPlayerMap.get(playerId);
	}

	public void updatePlayer(Player player) {
		// Noting to do :)
	}

	public boolean isRobotPlayer(Player player) {
		return player instanceof RobotPlayer;
	}

	public boolean isRobotPlayer(long playerId) {
		return robotPlayerMap.containsKey(playerId);
	}

	public Collection<RobotPlayer> getRobotPlayers() {
		return Collections.unmodifiableCollection(robotPlayerMap.values());
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
