package wisematches.playground;

import wisematches.core.RobotType;
import wisematches.core.personality.DefaultRobot;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockPlayerHand extends AbstractPlayerHand {
	public MockPlayerHand(short points, short oldRating, short newRating) {
		super(new DefaultRobot(RobotType.DULL), points, oldRating, newRating);
	}
}
