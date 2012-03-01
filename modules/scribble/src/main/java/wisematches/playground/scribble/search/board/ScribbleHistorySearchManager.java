package wisematches.playground.scribble.search.board;

import wisematches.playground.search.SearchCriteria;
import wisematches.playground.search.descriptive.AbstractDescriptiveSearchManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleHistorySearchManager extends AbstractDescriptiveSearchManager<ScribbleHistoryEntity, Void> {
	public ScribbleHistorySearchManager() {
		super(ScribbleHistoryEntity.class, true);
	}

	@Override
	protected String getEntitiesList(Void context, SearchCriteria[] criteria) {
		return "scribble_board as board, scribble_player as l, scribble_player as r";
	}

	@Override
	protected String getWhereCriterias(Void context, SearchCriteria[] criteria) {
		return "l.boardId=board.boardId and board.boardId=r.boardId and not board.resolution is null and l.playerId=:pid";
	}

	@Override
	protected String getGroupCriterias(Void context, SearchCriteria[] criteria) {
		return "board.boardId";
	}
}
