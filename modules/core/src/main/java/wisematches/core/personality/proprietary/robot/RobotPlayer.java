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
	public static final RobotPlayer DULL = new RobotPlayer(2, RobotType.DULL);

	public static final RobotPlayer TRAINEE = new RobotPlayer(3, RobotType.TRAINEE);

	public static final RobotPlayer EXPERT = new RobotPlayer(4, RobotType.EXPERT);

	private final RobotType robotType;

	private RobotPlayer(long id, RobotType robotType) {
		super(id, robotType.name().toLowerCase(), PlayerType.ROBOT);
		this.robotType = robotType;
	}

	public RobotType getRobotType() {
		return robotType;
	}

	public static RobotPlayer byId(long pid) {
		if (pid == DULL.getId()) {
			return DULL;
		} else if (pid == TRAINEE.getId()) {
			return TRAINEE;
		} else if (pid == EXPERT.getId()) {
			return EXPERT;
		}
		return null;
	}
}
