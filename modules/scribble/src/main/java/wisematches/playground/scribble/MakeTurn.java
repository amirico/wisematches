package wisematches.playground.scribble;

import wisematches.core.personality.Player;
import wisematches.playground.GameMove;

import java.util.Date;

/**
 * Scribble move has a few parameters: letters of the putted word, start position and word direction.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class MakeTurn extends GameMove {
	private final Word word;

	public MakeTurn(Player player, int points, int moveNumber, Date moveTime, Word word) {
		super(player, points, moveNumber, moveTime);
		this.word = word;
	}

	public Word getWord() {
		return word;
	}
}
