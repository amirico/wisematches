package wisematches.playground;

import wisematches.core.Personality;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockMove extends GameMove {
	public MockMove(Personality player) {
		super(player);
	}

	public MockMove(Personality player, int points, Date moveTime) {
		this(player);
		finalizeMove(points, 0, moveTime);
	}
}
