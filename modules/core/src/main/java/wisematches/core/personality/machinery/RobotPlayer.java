package wisematches.core.personality.machinery;

import wisematches.core.Machinery;

/**
 * This interface indicates that player is robot.
 * <p/>
 * {@code wisematches.server.core.robot.RobotPlayer} if localized player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class RobotPlayer extends Machinery {
	static final RobotPlayer DULL = new RobotPlayer(100, RobotType.DULL);
	static final RobotPlayer TRAINEE = new RobotPlayer(101, RobotType.TRAINEE);
	static final RobotPlayer EXPERT = new RobotPlayer(102, RobotType.EXPERT);

	private final RobotType robotType;

	private RobotPlayer(long id, RobotType robotType) {
		super(id);
		this.robotType = robotType;
	}

	public RobotType getRobotType() {
		return robotType;
	}
}
