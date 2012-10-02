package wisematches.playground.scribble;

import wisematches.playground.IncorrectMoveException;

/**
 * Indicates that exchange operation can't be done.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class IncorrectExchangeException extends IncorrectMoveException {
	private final Type type;

	public IncorrectExchangeException(String message, Type type) {
		super(message);
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	/**
	 * Indicates what exactly is borken.
	 */
	public static enum Type {
		/**
		 * There are no tiles selected
		 */
		EMPTY_TILES,
		/**
		 * Provided tiles can't be exchanged because player doesn't have them
		 */
		UNKNOWN_TILES,
		/**
		 * There are no required tiles number in a bank.
		 */
		EMPTY_BANK
	}
}
