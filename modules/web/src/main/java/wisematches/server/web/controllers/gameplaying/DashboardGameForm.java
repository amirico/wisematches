package wisematches.server.web.controllers.gameplaying;

import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribbleSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DashboardGameForm {
	private long boardId;
	private ScribbleSettings gameSettings;

	public DashboardGameForm(ScribbleBoard activeBoard) {
		boardId = activeBoard.getBoardId();
		gameSettings = activeBoard.getGameSettings();
	}

	public long getBoardId() {
		return boardId;
	}

	public ScribbleSettings getGameSettings() {
		return gameSettings;
	}
}
