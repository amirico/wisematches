package wisematches.server.gameplaying.room;

import wisematches.server.gameplaying.board.GameBoard;

/**
 * Contains information about expiring board.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class ExpiringBoardInfo {
	private final long boardId;
	private final int daysPerMove;
	private final long lastMoveTime;

	public ExpiringBoardInfo(GameBoard board) {
		this(board.getBoardId(), board.getGameSettings().getDaysPerMove(), board.getLastMoveTime());
	}

	public ExpiringBoardInfo(long boardId, int daysPerMove, long lastMoveTime) {
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

	public long getLastMoveTime() {
		return lastMoveTime;
	}

	@Override
	public String toString() {
		return "ExpiringBoardInfo{" +
				"boardId=" + boardId +
				", daysPerMove=" + daysPerMove +
				", lastMoveTime=" + lastMoveTime +
				'}';
	}
}
