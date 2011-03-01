package wisematches.server.gameplaying.scribble.room;

import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.board.BoardManager;
import wisematches.server.gameplaying.room.propose.GameProposalManager;
import wisematches.server.gameplaying.room.search.BoardsSearchEngine;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;
import wisematches.server.gameplaying.scribble.room.proposal.ScribbleProposal;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class ScribbleRoomManager implements RoomManager<ScribbleProposal, ScribbleSettings, ScribbleBoard> {
	private BoardsSearchEngine<ScribbleBoard> searchEngine;
	private GameProposalManager<ScribbleProposal> gameProposalManager;
	private BoardManager<ScribbleSettings, ScribbleBoard> boardManager;

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
	public BoardsSearchEngine<ScribbleBoard> getSearchesEngine() {
		return searchEngine;
	}

	@Override
	public GameProposalManager<ScribbleProposal> getGameProposalManager() {
		return gameProposalManager;
	}

	public void setSearchEngine(BoardsSearchEngine<ScribbleBoard> searchEngine) {
		this.searchEngine = searchEngine;
	}

	public void setProposalManager(GameProposalManager<ScribbleProposal> gameProposalManager) {
		this.gameProposalManager = gameProposalManager;
	}

	public void setBoardManager(BoardManager<ScribbleSettings, ScribbleBoard> boardManager) {
		this.boardManager = boardManager;
	}
}
