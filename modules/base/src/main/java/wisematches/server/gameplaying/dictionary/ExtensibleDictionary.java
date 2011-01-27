package wisematches.server.gameplaying.dictionary;

/**
 * Instabce of this dictionary can add or remove words.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ExtensibleDictionary extends Dictionary {
	void addDictionaryModificationListener(DictionaryModificationListener l);

	void removeDictionaryModificationListener(DictionaryModificationListener l);

	/**
	 * Adds new word to this dictionary.
	 *
	 * @param word the word that must be added.
	 * @return <code>true</code> if word was added; <code>false</code> if word already exist in this dictionary.
	 * @throws DictionaryModificationException
	 *                                  if dictionary can't be updated.
	 * @throws IllegalArgumentException if specified word has another locale than dictionary.
	 */
	boolean addWord(Word word) throws DictionaryModificationException;

	/**
	 * Removes specified word from dictionary.
	 *
	 * @param word the word that must be removed.
	 * @return <code>true</code> if word was removed; <code>false</code> if word is unknown dictionary.
	 * @throws DictionaryModificationException
	 *                                  if dictionary can't be updated.
	 * @throws IllegalArgumentException if specified word has another locale than dictionary.
	 */
	boolean removeWord(Word word) throws DictionaryModificationException;

	/**
	 * Removes specified word from dictionary.
	 *
	 * @param oldWord the word that must be updated.
	 * @param newWord the word with updated information.
	 * @return <code>true</code> if word was updated; <code>false</code> otherwise.
	 * @throws DictionaryModificationException
	 *                                  if dictionary can't be updated.
	 * @throws IllegalArgumentException see {@link #addWord(Word)} and
	 *                                  {@link #removeWord(Word)} methods to
	 *                                  get information about this exception.
	 */
	boolean updateWord(Word oldWord, Word newWord) throws DictionaryModificationException;
}
