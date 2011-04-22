package wisematches.server.gameplaying.scribble.room;

import wisematches.server.gameplaying.board.BoardManager;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.propose.GameProposalManager;
import wisematches.server.gameplaying.scribble.proposal.ScribbleProposal;
import wisematches.server.gameplaying.search.BoardsSearchEngine;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScribbleRoomManager implements RoomManager<ScribbleSettings, ScribbleProposal, ScribbleBoard> {
	private BoardsSearchEngine searchEngine;
	private BoardManager<ScribbleSettings, ScribbleBoard> boardManager;
	private GameProposalManager<ScribbleSettings, ScribbleProposal> gameProposalManager;

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
	public GameProposalManager<ScribbleSettings, ScribbleProposal> getProposalManager() {
		return gameProposalManager;
	}

	public void setSearchEngine(BoardsSearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	public void setProposalManager(GameProposalManager<ScribbleSettings, ScribbleProposal> gameProposalManager) {
		this.gameProposalManager = gameProposalManager;
	}

	public void setBoardManager(BoardManager<ScribbleSettings, ScribbleBoard> boardManager) {
		this.boardManager = boardManager;
	}
}
