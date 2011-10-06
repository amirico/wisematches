package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameRatingChange {
	private long playerId;

	private short points;

	private short oldRating;

	private short newRating;

	GameRatingChange() {
	}

	GameRatingChange(GamePlayerHand hand) {
		this(hand.getPlayerId(), hand.getPoints(), hand.getOldRating(), hand.getNewRating());
	}

	public GameRatingChange(long playerId, short points, short oldRating, short newRating) {
		this.playerId = playerId;
		this.points = points;
		this.oldRating = oldRating;
		this.newRating = newRating;
	}

	public long getPlayerId() {
		return playerId;
	}

	public short getPoints() {
		return points;
	}

	public short getOldRating() {
		return oldRating;
	}

	public short getNewRating() {
		return newRating;
	}

	public short getRatingDelta() {
		return (short) (newRating - oldRating);
	}

	@Override
	public String toString() {
		return "GameRatingChange{" +
				"playerId=" + playerId +
				", points=" + points +
				", oldRating=" + oldRating +
				", newRating=" + newRating +
				'}';
	}
}
