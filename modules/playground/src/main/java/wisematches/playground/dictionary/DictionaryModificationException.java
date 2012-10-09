package wisematches.playground.dictionary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DictionaryModificationException extends DictionaryException {
	public DictionaryModificationException(String message) {
		super(message);
	}

	public DictionaryModificationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DictionaryModificationException(Throwable cause) {
		super(cause);
	}
}
