package wisematches.core;

/**
 * Type of the robot.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum RobotType {
	/**
	 * Very simple robot.
	 */
	DULL(100, (short) 800),

	/**
	 * Medium brain robot.
	 */
	TRAINEE(101, (short) 1200),

	/**
	 * Very intelligent robot.
	 */
	EXPERT(102, (short) 1600);

	private final Robot player;

	private RobotType(long id, short rating) {
		this.player = new Robot(id, rating, this);
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


	public Robot getPlayer() {
		return player;
	}
}