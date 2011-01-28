package wisematches.server.player.computer.robot;

import wisematches.server.player.computer.ComputerPlayer;

import java.util.Arrays;
import java.util.Collection;

/**
 * This interface indicates that player is robot.
 * <p/>
 * {@code wisematches.server.core.robot.RobotPlayer} if localized player.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotPlayer extends ComputerPlayer {
	private final RobotType robotType;

	public static final RobotPlayer DULL = new RobotPlayer(2, "Robot Dull", 800, RobotType.DULL);
	public static final RobotPlayer TRAINEE = new RobotPlayer(3, "Robot Trainee", 1200, RobotType.TRAINEE);
	public static final RobotPlayer EXPERT = new RobotPlayer(4, "Robot Expert", 1800, RobotType.EXPERT);

	private RobotPlayer(long id, String nickname, int rating, RobotType robotType) {
		super(id, nickname, rating);
		this.robotType = robotType;
	}

	/**
	 * Returns type of this robot.
	 *
	 * @return the type of the player.
	 */
	public RobotType getRobotType() {
		return robotType;
	}

	public static RobotPlayer valueOf(RobotType type) {
		if (type == null) {
			throw new IllegalArgumentException("Type can't be null");
		}
		switch (type) {
			case DULL:
				return DULL;
			case TRAINEE:
				return TRAINEE;
			case EXPERT:
				return EXPERT;
			default:
				return null;
		}
	}

	public static Collection<RobotPlayer> getRobotPlayers() {
		return Arrays.asList(DULL, TRAINEE, EXPERT);
	}
}