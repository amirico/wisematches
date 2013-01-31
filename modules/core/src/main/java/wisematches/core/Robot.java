package wisematches.core;

/**
 * This interface indicates that player is robot.
 * <p/>
 * {@code wisematches.server.core.robot.RobotPlayer} if localized player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class Robot extends Personality {
	private final short rating;
	private final RobotType robotType;

	Robot(long id, short rating, RobotType robotType) {
		super(id);
		this.rating = rating;
		this.robotType = robotType;
	}

	public short getRating() {
		return rating;
	}

	public RobotType getRobotType() {
		return robotType;
	}
}
