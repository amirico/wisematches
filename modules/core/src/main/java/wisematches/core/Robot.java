package wisematches.core;

/**
 * This interface indicates that player is robot.
 * <p/>
 * {@code wisematches.server.core.robot.RobotPlayer} if localized player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class Robot extends Personality {
	private final RobotType robotType;

	protected Robot(RobotType robotType) {
		super(100 + robotType.ordinal());
		this.robotType = robotType;
	}

	public short getRating() {
		return robotType.getRating();
	}

	public RobotType getRobotType() {
		return robotType;
	}

	@Override
	public final boolean isTraceable() {
		return false;
	}
}
