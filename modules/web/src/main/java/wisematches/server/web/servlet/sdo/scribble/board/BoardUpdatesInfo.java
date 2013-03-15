package wisematches.server.web.servlet.sdo.scribble.board;

import wisematches.core.Personality;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.Tile;

import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardUpdatesInfo {
	private final Tile[] handTiles;
	private final MoveInfo[] moves;
	private final StatusInfo status;

	private final long generated;
	private final ScribbleBoard board;

	public BoardUpdatesInfo(ScribbleBoard board, StatusInfo status, MoveInfo[] moves, Tile[] tiles) {
		this.board = board;
		this.moves = moves;
		this.status = status;
		this.handTiles = tiles;
		generated = System.currentTimeMillis();
	}

	public long getGenerated() {
		return generated;
	}

	public MoveInfo[] getMoves() {
		return moves;
	}

	public StatusInfo getStatus() {
		return status;
	}

	public Tile[] getHandTiles() {
		return handTiles;
	}

	public ScoreInfo[] getScore() {
		final List<Personality> players = board.getPlayers();

		int index = 0;
		final ScoreInfo[] res = new ScoreInfo[players.size()];
		for (Personality player : players) {
			res[index++] = new ScoreInfo(board.getPlayerHand(player));
		}
		return res;
	}
}
