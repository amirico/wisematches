package wisematches.server.web.servlet.sdo.scribble.board;

import wisematches.playground.GameMessageSource;
import wisematches.playground.scribble.*;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MoveInfo extends InternationalisedInfo {
	private final ScribbleMove move;

	public MoveInfo(ScribbleMove move, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.move = move;
	}

	public int getNumber() {
		return move.getMoveNumber();
	}

	public int getPoints() {
		return move.getPoints();
	}

	public Long getPlayer() {
		return move.getPlayer().getId();
	}

	public TimeInfo getTime() {
		return new TimeInfo(move.getMoveTime(), messageSource, locale);
	}

	public Word getWord() {
		if (move instanceof MakeTurn) {
			final MakeTurn makeTurn = (MakeTurn) move;
			return makeTurn.getWord();
		}
		return null;
	}

	public int getExchange() {
		if (move instanceof ExchangeTiles) {
			ExchangeTiles exchangeMove = (ExchangeTiles) move;
			return exchangeMove.getTileIds().length;
		}
		return 0;
	}

	public ScribbleMoveType getType() {
		return move.getMoveType();
	}


	public static MoveInfo[] getMovesInfo(ScribbleBoard board, int fromPosition, GameMessageSource messageSource, Locale locale) {
		final List<ScribbleMove> gameMoves1 = board.getGameMoves();

		int index = 0;
		final List<ScribbleMove> gameMoves = gameMoves1.subList(fromPosition, gameMoves1.size());
		MoveInfo[] moves = new MoveInfo[gameMoves.size()];
		for (ScribbleMove gameMove : gameMoves) {
			moves[index++] = new MoveInfo(gameMove, messageSource, locale);
		}
		return moves;
	}
}
