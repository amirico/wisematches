package wisematches.playground.scribble;

import wisematches.core.Personality;

import java.util.Date;

/**
 * Scribble move has a few parameters: letters of the putted word, start position and word direction.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class MakeTurn extends ScribbleMove {
	private final Word word;

	protected MakeTurn(Personality player, Word word) {
		super(player);
		this.word = word;
	}

	public MakeTurn(Personality player, int points, Date moveTime, Word word) {
		super(player, points, moveTime);
		this.word = word;
	}

	public Word getWord() {
		return word;
	}

	@Override
	public ScribbleMoveType getMoveType() {
		return ScribbleMoveType.MAKE;
	}
}
