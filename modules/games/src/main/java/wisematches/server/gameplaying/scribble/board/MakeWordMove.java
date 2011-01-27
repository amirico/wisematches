package wisematches.server.gameplaying.scribble.board;

import wisematches.server.gameplaying.board.MakeTurnMove;
import wisematches.server.gameplaying.scribble.Word;

/**
 * Scrable move has a few parameters: letters of the putted word, start position and word direction.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class MakeWordMove extends MakeTurnMove {
	private final Word word;

	public MakeWordMove(long gamePlayer, Word word) {
		super(gamePlayer);
		if (word == null) {
			throw new NullPointerException("Word can't be null");
		}
		this.word = word;
	}

	public Word getWord() {
		return word;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		MakeWordMove move = (MakeWordMove) o;
		return word.equals(move.word);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + word.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "ScribbleMove{" +
				"word=" + word +
				", gamePlayer=" + getPlayerId() +
				'}';
	}
}
