package wisematches.playground;

import wisematches.core.Personality;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
@MappedSuperclass
public class AbstractPlayerHand implements GamePlayerHand {
	@Column(name = "playerId", updatable = false)
	private long playerId;

	@Column(name = "points")
	private short points;

	@Column(name = "winner")
	private boolean winner;

	@Column(name = "oldRating")
	private short oldRating;

	@Column(name = "newRating")
	private short newRating;

	protected AbstractPlayerHand() {
	}

	protected AbstractPlayerHand(Personality player) {
		this(player, (short) 0);
	}

	protected AbstractPlayerHand(Personality player, short points) {
		this.playerId = player.getId();
		this.points = points;
	}

	protected AbstractPlayerHand(Personality player, short points, short oldRating, short newRating) {
		this.playerId = player.getId();
		this.points = points;
		this.oldRating = oldRating;
		this.newRating = newRating;
	}

	@Override
	public final short getPoints() {
		return points;
	}

	@Override
	public final short getOldRating() {
		return oldRating;
	}

	@Override
	public final short getNewRating() {
		return newRating;
	}

	@Override
	public final boolean isWinner() {
		return winner;
	}

	long getPlayerId() {
		return playerId;
	}

	short increasePoints(short delta) {
		points = (short) (points + delta);
		return points;
	}

	public void markAsWinner() {
		this.winner = true;
	}

	public void updateRating(short oldRating, short newRating) {
		this.oldRating = oldRating;
		this.newRating = newRating;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AbstractPlayerHand");
		sb.append("{playerId=").append(playerId);
		sb.append(", points=").append(points);
		sb.append(", winner=").append(winner);
		sb.append(", oldRating=").append(oldRating);
		sb.append(", newRating=").append(newRating);
		sb.append('}');
		return sb.toString();
	}
}
