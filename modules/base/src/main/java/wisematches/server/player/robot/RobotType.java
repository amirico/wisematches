package wisematches.server.player.robot;

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
	EXPERT(1800);

	private final int rating;

	RobotType(int rating) {
		this.rating = rating;
	}

	/**
	 * Returns predefined rating for this robot type
	 *
	 * @return the rating for this robot
	 */
	public int getRating() {
		return rating;
	}
}
