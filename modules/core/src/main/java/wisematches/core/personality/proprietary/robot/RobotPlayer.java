package wisematches.core.personality.proprietary.robot;

import wisematches.core.personality.PlayerType;
import wisematches.core.personality.proprietary.ProprietaryPlayer;

/**
 * This interface indicates that player is robot.
 * <p/>
 * {@code wisematches.server.core.robot.RobotPlayer} if localized player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class RobotPlayer extends ProprietaryPlayer {
	public static final RobotPlayer DULL = new RobotPlayer(2, "robot.dull");
	public static final RobotPlayer TRAINEE = new RobotPlayer(3, "robot.trainee");
	public static final RobotPlayer EXPERT = new RobotPlayer(4, "robot.expert");

	private RobotPlayer(long id, String nickname) {
		super(id, nickname, PlayerType.ROBOT);
	}
}
