package wisematches.server.robot;

import wisematches.server.player.Player;

/**
 * This interface indicates that player is robot.
 * <p/>
 * {@code wisematches.server.core.robot.RobotPlayer} if localized player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RobotPlayer extends Player {
	/**
	 * Returns type of this robot.
	 *
	 * @return the type of the player.
	 */
	RobotType getRobotType();
}