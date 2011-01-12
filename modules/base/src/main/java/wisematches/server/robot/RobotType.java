package wisematches.server.robot;

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
	STAGER(1200),

	/**
	 * Very intelligent robot.
	 */
	EXPERT(1800);

	private final int rating;

	RobotType(int rating) {
		this.rating = rating;
	}

	public int getRating() {
		return rating;
	}
}
