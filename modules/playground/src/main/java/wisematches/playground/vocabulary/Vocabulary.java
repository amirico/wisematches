package wisematches.playground.vocabulary;

import wisematches.personality.Language;

import java.util.Collection;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Vocabulary extends Iterable<Word> {
	/**
	 * Returns unique id for this vocabulary.
	 *
	 * @return unique id for this vocabulary.
	 */
	String getId();

	/**
	 * Returns name of this vocabulary.
	 *
	 * @return name of this vocabulary.
	 */
	String getName();

	/**
	 * Returns language for this vocabulary.
	 *
	 * @return language for this vocabulary.
	 */
	Language getLanguage();

	/**
	 * Returns description of vocabulary.
	 *
	 * @return description of vocabulary.
	 */
	String getDescription();

	/**
	 * Returns date when the vocabulary was updated last time.
	 *
	 * @return date when the vocabulary was updated last time.
	 */
	Date getModificationDate();

	/**
	 * Returns number of words in this vocabulary.
	 *
	 * @return the number of words in this vocabulary.
	 */
	int getSize();

	/**
	 * Returns vocabulary word by it's name.
	 *
	 * @param name the word's name.
	 * @return vocabulary word or {@code null} if it's unknown.
	 */
	Word getWord(String name);

	/**
	 * Searches all vocabulary words by specified prefix.
	 *
	 * @param prefix search prefix.
	 * @return collection of words which are started with specified prefix. If there are no
	 *         words empty list will be returned.
	 */
	Collection<Word> searchWords(String prefix);
}
