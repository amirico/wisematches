package wisematches.client.android.app.playground.scribble.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleGame {
	private boolean active = true;

	private final long boardId;
	private final ScoreEngine scoreEngine;
	private final ScribbleBank scribbleBank;
	private final ScribbleSettings settings;
	private final List<ScribbleMove> moves;
	private final List<ScribblePlayer> players;

	private final Set<Integer> placedTiles = new HashSet<>();

	public ScribbleGame(long boardId, ScribbleSettings settings, ScribbleBank scribbleBank, List<ScribblePlayer> players, List<ScribbleMove> moves, ScoreEngine scoreEngine) {
		this.boardId = boardId;
		this.settings = settings;
		this.players = players;
		this.scribbleBank = scribbleBank;
		this.moves = moves;
		this.scoreEngine = scoreEngine;

		for (ScribbleMove move : moves) {
			if (move instanceof ScribbleMove.Make) {
				final ScribbleMove.Make make = (ScribbleMove.Make) move;

				final ScribbleWord word = make.getWord();
				for (ScribbleTile tile : word.getSelectedTiles()) {
					placedTiles.add(tile.getNumber());
				}
			}
		}
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

	public ScribbleBank getScribbleBank() {
		return scribbleBank;
	}


	public boolean isActive() {
		return active;
	}

	public int getBoardTilesCount() {
		return placedTiles.size();
	}
}
