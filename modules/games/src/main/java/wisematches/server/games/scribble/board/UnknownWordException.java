package wisematches.server.games.scribble.board;

import wisematches.server.games.board.IncorrectMoveException;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class UnknownWordException extends IncorrectMoveException {
	private final String word;

	public UnknownWordException(String word) {
		super("Word '" + word + "' is unknown");
		this.word = word;
	}

	public String getWord() {
		return word;
	}
}
