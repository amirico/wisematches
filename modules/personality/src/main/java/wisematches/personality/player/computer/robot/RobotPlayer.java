package wisematches.personality.player.computer.robot;

import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.personality.player.computer.ComputerPlayer;

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

	public static final RobotPlayer DULL = new RobotPlayer(2, "dull", RobotType.DULL, 800);
	public static final RobotPlayer TRAINEE = new RobotPlayer(3, "trainee", RobotType.TRAINEE, 1200);
	public static final RobotPlayer EXPERT = new RobotPlayer(4, "expert", RobotType.EXPERT, 1600);

	private RobotPlayer(long id, String nickname, RobotType robotType, int rating) {
		super(id, nickname, Membership.ROBOT, (short) rating);
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

	public static boolean isRobotPlayer(Personality personality) {
		return personality.getId() >= 2 && personality.getId() <= 4;
	}

	public static Collection<RobotPlayer> getRobotPlayers() {
		return Arrays.asList(DULL, TRAINEE, EXPERT);
	}
}