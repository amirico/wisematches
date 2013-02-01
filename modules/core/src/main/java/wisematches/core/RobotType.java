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
	DULL(800),

	/**
	 * Medium brain robot.
	 */
	TRAINEE(1200),

	/**
	 * Very intelligent robot.
	 */
	EXPERT(1600);

	private final short rating;

	private RobotType(int rating) {
		this.rating = (short) rating;
	}

	public short getRating() {
		return rating;
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
}