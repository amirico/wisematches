package wisematches.playground.tournament.impl.fsm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FinalizingGame implements TSMActivityContext {
	private final long boardId;

	public FinalizingGame(long boardId) {
		this.boardId = boardId;
	}

	public long getBoardId() {
		return boardId;
	}
}
