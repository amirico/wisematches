package wisematches.server.gameplaying.dictionary;

/**
 * This exception is thrown when requires dictionary not found
 */
public class DictionaryNotFoundException extends DictionaryModificationException {
	public DictionaryNotFoundException(String message) {
		super(message);
	}

	public DictionaryNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DictionaryNotFoundException(Throwable cause) {
		super(cause);
	}
}