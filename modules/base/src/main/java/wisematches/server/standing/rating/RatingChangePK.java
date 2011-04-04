package wisematches.server.standing.rating;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
class RatingChangePK implements Serializable {
	private long playerId;

	private long boardId;

	@Deprecated
	RatingChangePK() {
	}

	public RatingChangePK(long playerId, long boardId) {
		this.playerId = playerId;
		this.boardId = boardId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RatingChangePK)) return false;

		RatingChangePK that = (RatingChangePK) o;
		return boardId == that.boardId && playerId == that.playerId;
	}

	@Override
	public int hashCode() {
		int result = (int) (playerId ^ (playerId >>> 32));
		result = 31 * result + (int) (boardId ^ (boardId >>> 32));
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("RatingChangePK");
		sb.append("{playerId=").append(playerId);
		sb.append(", boardId=").append(boardId);
		sb.append('}');
		return sb.toString();
	}
}
