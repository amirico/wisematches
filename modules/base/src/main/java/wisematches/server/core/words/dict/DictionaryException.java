package wisematches.server.core.words.dict;

import wisematches.server.core.WisematchesException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DictionaryException extends WisematchesException {
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
