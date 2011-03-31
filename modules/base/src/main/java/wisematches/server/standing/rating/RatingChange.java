package wisematches.server.standing.rating;

import java.util.Date;

/**
 * An instance of this class contains information about one change for player's rating.
 * <p/>
 * This class supports {@code Comparable} interface that sorts ratings by date.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RatingChange implements Comparable<RatingChange> {
	private long playerId;
	private long gameBoardId;
	private Date changeDate;
	private short oldRating;
	private short newRating;

	RatingChange() {
	}

	public RatingChange(long playerId, long gameBoardId, Date changeDate, short oldRating, short newRating) {
		this.playerId = playerId;
		this.gameBoardId = gameBoardId;
		this.changeDate = changeDate;
		this.oldRating = oldRating;
		this.newRating = newRating;
	}

	public long getPlayerId() {
		return playerId;
	}

	public long getGameBoardId() {
		return gameBoardId;
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("RatingChange");
		sb.append("{playerId=").append(playerId);
		sb.append(", gameBoardId=").append(gameBoardId);
		sb.append(", changeDate=").append(changeDate);
		sb.append(", oldRating=").append(oldRating);
		sb.append(", newRating=").append(newRating);
		sb.append('}');
		return sb.toString();
	}

	@Override
	public int compareTo(RatingChange o) {
		return changeDate.compareTo(o.changeDate);
	}
}
