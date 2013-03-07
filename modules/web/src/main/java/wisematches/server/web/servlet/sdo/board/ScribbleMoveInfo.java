package wisematches.server.web.servlet.sdo.board;

import wisematches.playground.GameMessageSource;
import wisematches.playground.scribble.*;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleMoveInfo extends InternationalisedInfo {
	private final ScribbleMove move;

	public ScribbleMoveInfo(ScribbleMove move, GameMessageSource messageSource, Locale locale) {
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

	public long getTimeMillis() {
		return move.getMoveTime().getTime();
	}

	public String getTimeMessage() {
		return messageSource.formatDate(move.getMoveTime(), locale);
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

	public int[] getTiles() {
		if (move instanceof ExchangeTiles) {
			ExchangeTiles exchangeMove = (ExchangeTiles) move;
			return exchangeMove.getTileIds();
		}
		return null;
	}
}
