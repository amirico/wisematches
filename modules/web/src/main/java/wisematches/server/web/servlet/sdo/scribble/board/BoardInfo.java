package wisematches.server.web.servlet.sdo.scribble.board;

import wisematches.playground.GameMessageSource;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleMove;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.bank.LettersDistribution;
import wisematches.playground.scribble.score.ScoreBonus;
import wisematches.server.services.state.PlayerStateManager;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardInfo extends DescriptionInfo {
	private final Tile[] handTiles;
	private final ScribbleBoard board;

	public BoardInfo(ScribbleBoard board, Tile[] handTiles, PlayerStateManager stateManager, GameMessageSource messageSource, Locale locale) {
		super(board, stateManager, messageSource, locale);
		this.board = board;
		this.handTiles = handTiles;
	}

	public Tile[] getHandTiles() {
		return handTiles;
	}

	public int getAllHandBonus() {
		return board.getScoreEngine().getAllHandBonus();
	}

	public ScoreBonus[] getBonuses() {
		return board.getScoreEngine().getScoreBonuses();
	}

	public LettersDistribution getBank() {
		return board.getDistribution();
	}

	public MoveInfo[] getMoves() {
		final List<ScribbleMove> gameMoves = board.getGameMoves();

		int index = 0;
		final MoveInfo[] res = new MoveInfo[gameMoves.size()];
		for (ScribbleMove gameMove : gameMoves) {
			res[index++] = new MoveInfo(gameMove, messageSource, locale);
		}
		return res;
	}
}
