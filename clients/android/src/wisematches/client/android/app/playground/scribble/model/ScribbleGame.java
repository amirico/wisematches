package wisematches.client.android.app.playground.scribble.model;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleGame {
	private final long boardId;
	private final ScoreEngine scoreEngine;
	private final ScribbleSettings settings;
	private final List<ScribbleMove> moves;
	private final List<ScribblePlayer> players;

	public ScribbleGame(long boardId, ScribbleSettings settings, List<ScribblePlayer> players, List<ScribbleMove> moves, ScoreEngine scoreEngine) {
		this.boardId = boardId;
		this.settings = settings;
		this.players = players;
		this.moves = moves;
		this.scoreEngine = scoreEngine;
	}

	public long getBoardId() {
		return boardId;
	}

	public ScribbleSettings getSettings() {
		return settings;
	}

	public List<ScribbleMove> getMoves() {
		return moves;
	}

	public List<ScribblePlayer> getPlayers() {
		return players;
	}

	public ScoreEngine getScoreEngine() {
		return scoreEngine;
	}
}
