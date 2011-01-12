package wisematches.server.games.dictionary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DictionaryException extends Exception {
	public DictionaryException(String message) {
		super(message);
	}

	public DictionaryException(String message, Throwable cause) {
		super(message, cause);
	}

	public DictionaryException(Throwable cause) {
		super(cause);
	}
}
