package wisematches.playground.search;

import wisematches.playground.GameBoard;

import java.util.Date;

/**
 * Contains information about expiring board.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class LastMoveInfo {
	private final long boardId;
	private final int daysPerMove;
	private final Date lastMoveTime;

	public LastMoveInfo(GameBoard board) {
		this(board.getBoardId(), board.getGameSettings().getDaysPerMove(), board.getLastMoveTime());
	}

	public LastMoveInfo(long boardId, int daysPerMove, Date lastMoveTime) {
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

	@Override
	public String toString() {
		return "LastMoveInfo{" +
				"boardId=" + boardId +
				", daysPerMove=" + daysPerMove +
				", lastMoveTime=" + lastMoveTime +
				'}';
	}
}
