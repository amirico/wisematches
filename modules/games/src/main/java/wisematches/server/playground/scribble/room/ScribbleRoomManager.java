package wisematches.server.playground.scribble.room;

import wisematches.server.playground.board.BoardManager;
import wisematches.server.playground.room.RoomManager;
import wisematches.server.playground.propose.GameProposalManager;
import wisematches.server.playground.search.BoardsSearchEngine;
import wisematches.server.playground.scribble.board.ScribbleBoard;
import wisematches.server.playground.scribble.board.ScribbleSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScribbleRoomManager implements RoomManager<ScribbleSettings, ScribbleBoard> {
	private BoardsSearchEngine searchEngine;
	private BoardManager<ScribbleSettings, ScribbleBoard> boardManager;
	private GameProposalManager<ScribbleSettings> gameProposalManager;

	public ScribbleRoomManager() {
	}

	@Override
	public ScribbleRoom getRoomType() {
		return ScribbleRoom.name;
	}

	@Override
	public BoardManager<ScribbleSettings, ScribbleBoard> getBoardManager() {
		return boardManager;
	}

	@Override
	public BoardsSearchEngine getSearchesEngine() {
		return searchEngine;
	}

	@Override
	public GameProposalManager<ScribbleSettings> getProposalManager() {
		return gameProposalManager;
	}

	public void setSearchEngine(BoardsSearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	public void setProposalManager(GameProposalManager<ScribbleSettings> gameProposalManager) {
		this.gameProposalManager = gameProposalManager;
	}

	public void setBoardManager(BoardManager<ScribbleSettings, ScribbleBoard> boardManager) {
		this.boardManager = boardManager;
	}
}
