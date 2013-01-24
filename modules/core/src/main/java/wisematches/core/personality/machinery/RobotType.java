package wisematches.core.personality.machinery;

/**
 * Type of the robot.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum RobotType {
	/**
	 * Very simple robot.
	 */
	DULL(RobotPlayer.DULL),

	/**
	 * Medium brain robot.
	 */
	TRAINEE(RobotPlayer.TRAINEE),

	/**
	 * Very intelligent robot.
	 */
	EXPERT(RobotPlayer.EXPERT);

	private final RobotPlayer player;

	private RobotType(RobotPlayer player) {
		this.player = player;
	}


	public boolean isDull() {
		return this == DULL;
	}

	public boolean isTrainee() {
		return this == TRAINEE;
	}

	public boolean isExpert() {
		return this == EXPERT;
	}


	public RobotPlayer getRobotPlayer() {
		return player;
	}
}