package wisematches.server.web.controllers.gameplaying;

import wisematches.server.gameplaying.scribble.board.ScribbleSettings;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DashboardGameForm {
	private long boardId;
	private ScribbleSettings settings;
	private List<PlayerGameForm> players;

	public DashboardGameForm(long boardId, ScribbleSettings settings, List<PlayerGameForm> players) {
		this.boardId = boardId;
		this.settings = settings;
		this.players = players;
	}

	public long getBoardId() {
		return boardId;
	}

	public ScribbleSettings getSettings() {
		return settings;
	}

	public List<PlayerGameForm> getPlayers() {
		return players;
	}
}
