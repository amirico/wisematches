package wisematches.playground.dictionary;

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
}
