package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockGameSettings extends GameSettings {
	public MockGameSettings(String title, int daysPerMove) {
		super(title, daysPerMove);
	}

	public MockGameSettings(String title, int daysPerMove, boolean rated, boolean scratch) {
		super(title, daysPerMove, rated, scratch);
	}

	@Override
	public int getMinPlayers() {
		return 2;
	}

	@Override
	public int getMaxPlayers() {
		return 4;
	}
}
