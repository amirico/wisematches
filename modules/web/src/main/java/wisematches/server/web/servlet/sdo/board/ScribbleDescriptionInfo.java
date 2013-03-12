package wisematches.server.web.servlet.sdo.board;

import wisematches.core.Personality;
import wisematches.playground.GameMessageSource;
import wisematches.playground.GameMove;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleMove;

import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleDescriptionInfo {
	private final ScribbleStateInfo state;
	private final ScribbleHandInfo[] hands;
	private final ScribbleMoveInfo[] moves;

	public ScribbleDescriptionInfo(Personality person, ScribbleBoard board, int movesCount, GameMessageSource messageSource, Locale locale) {
		state = new ScribbleStateInfo(board, messageSource, locale);

		final List<ScribbleMove> gameMoves = board.getGameMoves();


		if (gameMoves.size() > movesCount) {
			int index = 0;
			moves = new ScribbleMoveInfo[gameMoves.size() - movesCount];
			final ListIterator<ScribbleMove> gameMoveListIterator = gameMoves.listIterator(movesCount);
			while (gameMoveListIterator.hasNext()) {
				final GameMove move = gameMoveListIterator.next();
				moves[index++] = new ScribbleMoveInfo((ScribbleMove) move, messageSource, locale);
			}
		} else {
			moves = null;
		}

		int index = 0;
		final List<Personality> players = board.getPlayers();
		hands = new ScribbleHandInfo[players.size()];
		for (Personality player : players) {
			hands[index++] = new ScribbleHandInfo(player, board.getPlayerHand(player), player.equals(person));
		}
	}

	public ScribbleStateInfo getState() {
		return state;
	}

	public ScribbleHandInfo[] getHands() {
		return hands;
	}

	public ScribbleMoveInfo[] getMoves() {
		return moves;
	}
}
