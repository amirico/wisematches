package wisematches.server.core.robot;

import wisematches.kernel.player.Player;
import wisematches.server.core.room.Room;
import wisematches.server.core.account.PlayerManager;

import java.util.Collection;

/**
 * Robots brain manager is a manager class that contains brains for all games that supports robots.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RobotsBrainManager extends PlayerManager {
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
     * Returns brain of specified type for specified room.
     *
     * @param room      the room id.
     * @param robotType the type of brain that should be returned.
     * @return the brain of specified type for specified room.
     * @throws IllegalArgumentException if room is unknown or room doesn't have brain of specified type.
     */
    RobotBrain getRobotBrain(Room room, RobotType robotType);

    /**
     * Returns robot with specified id.
     *
     * @param playerId the robot id.
     * @return the robot player.
     * @throw java.lang.IllegalArgumentException if player with specified id is not a robot.
     * @see #isRobotPlayer(long)
     */
    RobotPlayer getPlayer(long playerId);

    /**
     * Returns collection of all supported robot players.
     *
     * @return the unmodifiable collection of robot players.
     */
    Collection<RobotPlayer> getRobotPlayers();

    /**
     * Checks that specified player is robot. This method just checks that {@code player instanceof RobotPlayer}.
     *
     * @param player the player to be checked.
     * @return {@code true} if player is robot; {@code false} - otherwise.
     */
    boolean isRobotPlayer(Player player);

    /**
     * Checks that player with specified id is robot.
     *
     * @param playerId the player id to be checked.
     * @return {@code true} if player is robot; {@code false} - otherwise.
     */
    boolean isRobotPlayer(long playerId);
}