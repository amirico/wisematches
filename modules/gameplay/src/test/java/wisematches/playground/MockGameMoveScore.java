package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockGameMoveScore implements GameMoveScore {
	private short points;

	public MockGameMoveScore() {
	}

	@Override
	public short getPoints() {
		return points;
	}

	public void setPoints(short points) {
		this.points = points;
	}

	@Override
	public String getFormula() {
		return String.valueOf(points);
	}
}
