package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MockPlayerHand implements GamePlayerHand {
	private short points;
	private short oldRating;
	private short newRating;
	private boolean winner;

	public MockPlayerHand(short points, short oldRating, short newRating) {
		this(points, oldRating, newRating, false);
	}

	public MockPlayerHand(short points, short oldRating, short newRating, boolean winner) {
		this.points = points;
		this.oldRating = oldRating;
		this.newRating = newRating;
		this.winner = winner;
	}

	@Override
	public short getPoints() {
		return points;
	}

	@Override
	public short getOldRating() {
		return oldRating;
	}

	@Override
	public short getNewRating() {
		return newRating;
	}

	@Override
	public boolean isWinner() {
		return winner;
	}
}
