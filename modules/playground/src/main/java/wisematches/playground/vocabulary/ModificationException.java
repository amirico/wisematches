package wisematches.playground.vocabulary;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ModificationException extends Exception {
	public ModificationException(String message) {
		super(message);
	}

	public ModificationException(String message, Throwable cause) {
		super(message, cause);
	}
}
