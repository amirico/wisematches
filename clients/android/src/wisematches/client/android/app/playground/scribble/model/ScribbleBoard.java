package wisematches.client.android.app.playground.scribble.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleBoard {
	private final long boardId;
	private final ScribbleSettings settings;
	private final Map<Long, ScribblePlayer> players;
	private final List<ScribbleMove> moves;
	private final ScoreEngine scoreEngine;

	public ScribbleBoard(long boardId, ScribbleSettings settings, Map<Long, ScribblePlayer> players, List<ScribbleMove> moves, ScoreEngine scoreEngine) {
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

	public Collection<ScribblePlayer> getPlayers() {
		return players.values();
	}

	public ScoreEngine getScoreEngine() {
		return scoreEngine;
	}
}
