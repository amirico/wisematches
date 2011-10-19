package wisematches.playground.scribble.search.board;

import wisematches.playground.search.AbstractHibernateSearchManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleHistorySearchManager extends AbstractHibernateSearchManager<ScribbleHistoryEntity, Void> {
	public ScribbleHistorySearchManager() {
		super(ScribbleHistoryEntity.class, true);
	}

	@Override
	protected String getTablesList(Void context) {
		return "scribble_board as board, scribble_player as l, scribble_player as r";
	}

	@Override
	protected String getWhereCriterias(Void context) {
		return "l.boardId=board.boardId and board.boardId=r.boardId and not board.resolution is null and l.playerId=:pid";
	}

	@Override
	protected String getGroupCriterias(Void context) {
		return "board.boardId";
	}
}
