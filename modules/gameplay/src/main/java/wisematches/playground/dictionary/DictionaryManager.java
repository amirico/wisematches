package wisematches.playground.dictionary;

import wisematches.core.Language;

import java.util.Collection;

/**
 * The {@code DictionaryManager} provides access to dictionaries.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DictionaryManager {
	/**
	 * Returns collection of all supported languages.
	 *
	 * @return the unmodifiable collection of all supported languages.
	 */
	Collection<Language> getLanguages();

	/**
	 * Returns dictionary by specified language.
	 *
	 * @param language the language of the dictionary
	 * @return the dictionary for specified language or {@code null} if specified language is not supported by this manager.
	 */
	Dictionary getDictionary(Language language);
}
