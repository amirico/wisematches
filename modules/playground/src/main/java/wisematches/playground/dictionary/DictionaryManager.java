package wisematches.playground.dictionary;

import java.util.Locale;

/**
 * <code>DictionaryManager</code>
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionaryManager {
	Dictionary getDictionary(Locale locale) throws DictionaryNotFoundException;
}