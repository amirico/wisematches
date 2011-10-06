package wisematches.playground;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

/**
 * <code>GamePlayerHand</code> is a hand of the player. It contains information about player on the board, like
 * it's point, it's items in hand and so on.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
@MappedSuperclass
public class GamePlayerHand {
	@Column(name = "playerId", updatable = false)
	private long playerId;

	@Column(name = "points")
	private short points;

	@Column(name = "oldRating")
	private short oldRating;

	@Column(name = "newRating")
	private short newRating;

	/**
	 * This is Hibernate constructor. In subclasses in must be declared as package visibile.
	 */
	@Deprecated
	protected GamePlayerHand() {
	}

	/**
	 * Creates new player hand with specified player id and statr points.
	 *
	 * @param playerId the player id.
	 */
	protected GamePlayerHand(long playerId) {
		this.playerId = playerId;
	}

	public GamePlayerHand(long playerId, short points) {
		this.playerId = playerId;
		this.points = points;
	}

	/**
	 * Returns player id who has this hand.
	 *
	 * @return the player id
	 */
	public long getPlayerId() {
		return playerId;
	}

	/**
	 * Returns points of the player on the board.
	 *
	 * @return the player's points
	 */
	public short getPoints() {
		return points;
	}

	short getOldRating() {
		return oldRating;
	}

	short getNewRating() {
		return newRating;
	}

	/**
	 * Increases points of the player and returns new value.
	 *
	 * @param delta the delta
	 * @return increased points.
	 */
	short increasePoints(short delta) {
		points = (short) (points + delta);
		return points;
	}

	void changeRating(GameRatingChange change) {
		this.oldRating = change.getOldRating();
		this.newRating = change.getNewRating();
	}

	@Override
	public String toString() {
		return "playerId=" + getPlayerId() +
				", points=" + points;
	}
}
