package wisematches.playground.scribble.bank;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class UnsupportedLanguageException extends RuntimeException {
	public UnsupportedLanguageException(String message) {
		super(message);
	}

	public UnsupportedLanguageException(String message, Throwable cause) {
		super(message, cause);
	}
}
