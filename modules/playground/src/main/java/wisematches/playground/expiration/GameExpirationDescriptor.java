package wisematches.playground.expiration;

import wisematches.playground.GameBoard;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GameExpirationDescriptor {
	private final long boardId;
	private final int daysPerMove;
	private final Date lastMoveTime;

	private static final int MILLIS_IN_DAY = 86400000;//24 * 60 * 60 * 1000;

	public GameExpirationDescriptor(long boardId, int daysPerMove, Date lastMoveTime) {
		this.boardId = boardId;
		this.daysPerMove = daysPerMove;
		this.lastMoveTime = lastMoveTime;
	}

	public long getBoardId() {
		return boardId;
	}

	public int getDaysPerMove() {
		return daysPerMove;
	}

	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	public Date getExpiringDate() {
		return getExpiringDate(daysPerMove, lastMoveTime);
	}

	public static Date getExpiringDate(GameBoard board) {
		return getExpiringDate(board.getGameSettings().getDaysPerMove(), board.getLastMoveTime());
	}

	private static Date getExpiringDate(int daysPerMove, Date lastMoveTime) {
		return new Date(lastMoveTime.getTime() + daysPerMove * MILLIS_IN_DAY);
	}

	@Override
	public String toString() {
		return "GameExpirationDescriptor{" +
				"boardId=" + boardId +
				", daysPerMove=" + daysPerMove +
				", lastMoveTime=" + lastMoveTime +
				'}';
	}
}
