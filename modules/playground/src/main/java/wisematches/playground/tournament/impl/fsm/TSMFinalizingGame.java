package wisematches.playground.tournament.impl.fsm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TSMFinalizingGame implements TSMActivityContext {
	private final long boardId;

	public TSMFinalizingGame(long boardId) {
		this.boardId = boardId;
	}

	public long getBoardId() {
		return boardId;
	}
}
