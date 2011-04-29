package wisematches.server.playground.dictionary;

import java.util.Iterator;
import java.util.Locale;

/**
 * <code>WordGamesDictionaries</code> is base interface for using in word games. It represends a extended
 * <code>DictionaryManager</code> that declares two types of dictionaries: lixecal dictionary (all words approved
 * and known for most people) and slangly dictionary (words were offered by players and not approved by other people).
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface WordGamesDictionaries extends DictionaryManager {
	Dictionary getDictionary(Locale locale, boolean allowSlangWords) throws DictionaryNotFoundException;

	boolean addSlanglyWord(Word word) throws DictionaryModificationException;

	boolean removeSlanglyWord(Word word) throws DictionaryModificationException;

	boolean approveSlanglyWord(Word word) throws DictionaryModificationException;

	boolean doubtSlanglyWord(Word word) throws DictionaryModificationException;

	Iterator<String> iterateSlanglyWords(Locale locale) throws DictionaryNotFoundException;
}
