package wisematches.server.gameplaying.search;

import wisematches.server.gameplaying.board.GameBoard;

import java.util.Date;

/**
 * Contains information about expiring board.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class BoardLastMoveInfo {
	private final long boardId;
	private final int daysPerMove;
	private final Date lastMoveTime;

	public BoardLastMoveInfo(GameBoard board) {
		this(board.getBoardId(), board.getGameSettings().getDaysPerMove(), board.getLastMoveTime());
	}

	public BoardLastMoveInfo(long boardId, int daysPerMove, Date lastMoveTime) {
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
		return "BoardLastMoveInfo{" +
				"boardId=" + boardId +
				", daysPerMove=" + daysPerMove +
				", lastMoveTime=" + lastMoveTime +
				'}';
	}
}
