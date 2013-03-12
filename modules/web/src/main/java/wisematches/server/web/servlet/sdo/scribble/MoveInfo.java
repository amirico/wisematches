package wisematches.server.web.servlet.sdo.scribble;

import wisematches.playground.GameMessageSource;
import wisematches.playground.scribble.*;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

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

	public ScribbleMoveType getType() {
		return move.getMoveType();
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
}
