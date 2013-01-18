package wisematches.playground.scribble.history;

import wisematches.core.search.SearchFilter;
import wisematches.core.search.descriptive.AbstractDescriptiveSearchManager;
import wisematches.playground.GameResolution;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleHistorySearchManager extends AbstractDescriptiveSearchManager<ScribbleHistoryEntity, GameResolution, SearchFilter> {
	public ScribbleHistorySearchManager() {
		super(ScribbleHistoryEntity.class, true);
	}

	@Override
	protected String getEntitiesList(GameResolution resolution, SearchFilter filter) {
		return "scribble_board as board, scribble_player as l, scribble_player as r";
	}

	@Override
	protected String getWhereCriterias(GameResolution resolution, SearchFilter filter) {
		String s = "l.boardId=board.boardId and board.boardId=r.boardId and l.playerId=:pid";
		if (resolution != null) {
			s += " and board.resolution=" + resolution.ordinal();
		} else {
			s += " and not board.resolution is null";
		}
		return s;
	}

	@Override
	protected String getGroupCriterias(GameResolution resolution, SearchFilter filter) {
		return "board.boardId";
	}
}
