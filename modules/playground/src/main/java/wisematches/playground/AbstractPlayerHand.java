package wisematches.playground;

import wisematches.core.personality.Player;

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

	@Column(name = "oldRating")
	private short oldRating;

	@Column(name = "newRating")
	private short newRating;

	protected AbstractPlayerHand() {
	}

	protected AbstractPlayerHand(Player player) {
		this(player, (short) 0);
	}

	protected AbstractPlayerHand(Player player, short points) {
		this.playerId = player.getId();
		this.points = points;
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
		return newRating > oldRating;
	}

	long getPlayerId() {
		return playerId;
	}

	short increasePoints(short delta) {
		points = (short) (points + delta);
		return points;
	}

	void finalize(short oldRating, short newRating) {
		this.oldRating = oldRating;
		this.newRating = newRating;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AbstractPlayerHand");
		sb.append("{playerId=").append(playerId);
		sb.append(", points=").append(points);
		sb.append(", oldRating=").append(oldRating);
		sb.append(", newRating=").append(newRating);
		sb.append('}');
		return sb.toString();
	}
}
