package wisematches.server.standing.rating;

import javax.persistence.*;
import java.util.Date;

/**
 * An instance of this class contains information about one change for player's rating.
 * <p/>
 * This class supports {@code Comparable} interface that sorts ratings by date.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "rating_history")
@IdClass(RatingChangePK.class)
public class RatingChange implements Comparable<RatingChange> {
	@Id
	@Column(name = "playerId", nullable = false, updatable = false)
	private long playerId;

	@Id
	@Column(name = "boardId", nullable = false, updatable = false)
	private long boardId;

	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time", nullable = false, updatable = false)
	private Date changeDate;

	@Basic
	@Column(name = "oldRating", nullable = false, updatable = false)
	private short oldRating;

	@Basic
	@Column(name = "newRating", nullable = false, updatable = false)
	private short newRating;

	@Basic
	@Column(name = "points", nullable = false, updatable = false)
	private short points;

	/**
	 * Hibernate constructor only
	 */
	@Deprecated
	RatingChange() {
	}

	public RatingChange(long playerId, long boardId, Date changeDate, short oldRating, short newRating, short points) {
		this.playerId = playerId;
		this.boardId = boardId;
		this.changeDate = changeDate;
		this.oldRating = oldRating;
		this.newRating = newRating;
		this.points = points;
	}

	public long getPlayerId() {
		return playerId;
	}

	public long getBoardId() {
		return boardId;
	}

	public Date getChangeDate() {
		return changeDate;
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

	public short getPoints() {
		return points;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("RatingChange");
		sb.append("{playerId=").append(playerId);
		sb.append(", gameBoardId=").append(boardId);
		sb.append(", changeDate=").append(changeDate);
		sb.append(", oldRating=").append(oldRating);
		sb.append(", newRating=").append(newRating);
		sb.append(", points=").append(points);
		sb.append('}');
		return sb.toString();
	}

	@Override
	public int compareTo(RatingChange o) {
		return changeDate.compareTo(o.changeDate);
	}
}
