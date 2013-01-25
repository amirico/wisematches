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
	static final RobotPlayer DULL = new RobotPlayer(100, (short) 800, RobotType.DULL);
	static final RobotPlayer TRAINEE = new RobotPlayer(101, (short) 1200, RobotType.TRAINEE);
	static final RobotPlayer EXPERT = new RobotPlayer(102, (short) 1600, RobotType.EXPERT);

	private final short rating;
	private final RobotType robotType;

	private RobotPlayer(long id, short rating, RobotType robotType) {
		super(id);
		this.rating = rating;
		this.robotType = robotType;
	}

	public RobotType getRobotType() {
		return robotType;
	}

	@Override
	public short getRating() {
		return rating;
	}
}
