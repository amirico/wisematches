package wisematches.playground;

import wisematches.core.Personality;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockMove extends GameMove {
	public MockMove(Personality player) {
		this(player, 0, 0, new Date());
	}

	public MockMove(Personality player, int points, int moveNumber, Date moveTime) {
		super(player, points, moveNumber, moveTime);
	}
}
